package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
public class UserController extends GenericController<User> {
    
    private final UserRepository userRepository;
    
    public UserController(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }
}
