//package com.sber.java13.filmlibrary.rest.controller;
//
//import com.sber.java13.filmlibrary.config.jwt.JWTTokenUtil;
//import com.sber.java13.filmlibrary.dto.LoginDTO;
//import com.sber.java13.filmlibrary.dto.UserDTO;
//import com.sber.java13.filmlibrary.model.User;
//import com.sber.java13.filmlibrary.service.UserService;
//import com.sber.java13.filmlibrary.service.userdetails.CustomUserDetailsService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/users")
//@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
//@Slf4j
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class UserController extends GenericController<User, UserDTO> {
//
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JWTTokenUtil jwtTokenUtil;
//    private final UserService userService;
//
//    public UserController(UserService userService,
//                          CustomUserDetailsService customUserDetailsService,
//                          JWTTokenUtil jwtTokenUtil) {
//        super(userService);
//        this.customUserDetailsService = customUserDetailsService;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.userService = userService;
//    }
//
//    @PostMapping("/auth")
//    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) {
//        Map<String, Object> response = new HashMap<>();
//        log.info("LoginDTO: {}", loginDTO);
//        UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDTO.getLogin());
//        log.info("foundUser, {}", foundUser);
//        if (!userService.checkPassword(loginDTO.getPassword(), foundUser)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка авторизации!\nНеверный пароль");
//        }
//        String token = jwtTokenUtil.generateToken(foundUser);
//        response.put("token", token);
//        response.put("username", foundUser.getUsername());
//        response.put("authorities", foundUser.getAuthorities());
//        return ResponseEntity.ok().body(response);
//    }
//}
