package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.service.DirectorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@Rollback(value = false)
@AutoConfigureMockMvc
public class DirectorControllerTest extends CommonTestMVC {
    
    private final DirectorDTO directorDTO = new DirectorDTO("MVC_TestDirectorFio", "Test Position", new HashSet<>(), false);
    private final DirectorDTO directorDTOUpdated = new DirectorDTO("MVC_TestDirectorFio_UPDATED", "Test Position", new HashSet<>(), false);
    
    @Autowired
    private DirectorService directorService;
    
    @Test
    @DisplayName("Просмотр всех директоров через MVC контроллер, тестирование 'directors/'")
    @Order(0)
    @WithAnonymousUser
    @Override
    protected void getAll() throws Exception {
        log.info("Тест по выбору всех директоров через MVC начат");
        MvcResult result = mvc.perform(get("/directors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/viewAllDirectors"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }
    
    @Test
    @DisplayName("Просмотр одного директора через MVC контроллер, тестирование 'directors/{id}'")
    @Order(1)
    @WithAnonymousUser
    @Override
    protected void getOne() throws Exception {
        log.info("Тест по выбору одного директора через MVC начат");
        mvc.perform(MockMvcRequestBuilders.get("/directors/{id}", "1")
                        .param("director", "directorDTO")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("director"))
                .andExpect(view().name("directors/viewDirector"));
    }
    
    @Test
    @DisplayName("Создание директора через MVC контроллер, тестирование 'directors/add'")
    @Order(2)
//    @WithUserDetails(value = "mod", userDetailsServiceBeanName="customUserDetailsService")
    @WithMockUser(username = "mod", roles = "LIBRARIAN", password = "000")
    @Override
    protected void create() throws Exception {
        log.info("Тест по созданию директора через MVC начат успешно");
        mvc.perform(post("/directors/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", directorDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrlTemplate("/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по созданию директора через MVC закончен успешно");
    }
    
    @Order(3)
    @Test
    @DisplayName("Обновление директора через MVC контроллер, тестирование 'directors/update'")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Override
    protected void update() throws Exception {
        log.info("Тест по обновлению директора через MVC начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorsFio"));
        DirectorDTO foundDirectorForUpdate = directorService.searchDirectors(directorDTO.getDirectorsFio(), pageRequest).getContent().get(0);
        foundDirectorForUpdate.setDirectorsFio(directorDTOUpdated.getDirectorsFio());
        mvc.perform(post("/directors/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", foundDirectorForUpdate)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по обновлению директора через MVC закончен успешно");
    }
    
    @Order(4)
    @Test
    @DisplayName("Софт удаление директора через MVC контроллер, тестирование 'directors/delete'")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Override
    protected void delete() throws Exception {
        log.info("Тест по soft удалению директора через MVC начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorsFio"));
        DirectorDTO foundDirectorForDelete = directorService.searchDirectors(directorDTOUpdated.getDirectorsFio(), pageRequest).getContent().get(0);
        foundDirectorForDelete.setDeleted(true);
        mvc.perform(get("/directors/delete/{id}", foundDirectorForDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));
        DirectorDTO deletedDirector = directorService.getOne(foundDirectorForDelete.getId());
        assertTrue(deletedDirector.isDeleted());
        log.info("Тест по soft удалению директора через MVC закончен успешно");
    }
}
