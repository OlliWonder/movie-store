package com.sber.java13.filmlibrary.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sber.java13.filmlibrary.config.jwt.JWTTokenUtil;
import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.service.userdetails.CustomUserDetailsService;
import com.sber.java13.filmlibrary.model.Director;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DirectorControllerTest {
    @Autowired
    public MockMvc mvc;
    @Autowired
    private JWTTokenUtil jwtTokenUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private String token;
    private static final String ROLE_USER_NAME = "Olli";
    
    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        token = generateToken(ROLE_USER_NAME);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
    
    private String generateToken(String userName) {
        return jwtTokenUtil.generateToken(customUserDetailsService.loadUserByUsername(userName));
    }
    
    @Test
    public void listAllDirectors() throws Exception {
        String result = mvc.perform(
                        get("/rest/directors/list")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        List<Director> directors = mapper.readValue(result, new TypeReference<List<Director>>() {
        });
        System.out.println(directors.size());
    }
    
    @Test
    public void createUpdateDeleteTestDirector() throws Exception {
        DirectorDTO directorDTO = new DirectorDTO();
        directorDTO.setDirectorsFio("STEVEN SPIELBERG");
        directorDTO.setDirectorsPosition("GENERAL DIRECTOR");
        
        String response = mvc.perform(
                        post("/rest/directors/add")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(directorDTO))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Director director = objectMapper.readValue(response, Director.class);
        DirectorDTO directorDTO1 = new DirectorDTO();
        directorDTO1.setDirectorsFio("TESTEROV TEST TESTEROVICH UPDATED");
        directorDTO1.setDirectorsPosition("2022-2023 UPD");
        
        String responseNew = mvc.perform(
                        put("/rest/directors/update")
                                .param("directorId", director.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(directorDTO1))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        director = objectMapper.readValue(responseNew, Director.class);
        System.out.println(director);
        //delete test director from db
        
        String deleteResponse = mvc.perform(
                        delete("/rest/directors/delete")
                                .param("directorId", director.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(deleteResponse);
    }
    
    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}