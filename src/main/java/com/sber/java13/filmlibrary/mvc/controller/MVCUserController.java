package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

import static com.sber.java13.filmlibrary.constants.UserRoleConstants.ADMIN;

@Controller
@Hidden
@RequestMapping("/users")
public class MVCUserController {
    private final UserService userService;
    
    public MVCUserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDTO());
        return "registration";
    }
    
    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDTO userDTO, BindingResult bindingResult) {
        if (userDTO.getLogin().equalsIgnoreCase(ADMIN) || userService.getUserByLogin(userDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Такой логин уже существует!");
            return "registration";
        }
        if (userService.getUserByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Такой email уже существует!");
            return "registration";
        }
        userService.create(userDTO);
        return "redirect:/login";
    }
    
    @GetMapping("/remember-password")
    public String rememberPassword() {
        return "users/rememberPassword";
    }
    
    @PostMapping("/remember-password")
    public String rememberPassword(@ModelAttribute("changePasswordForm") UserDTO userDTO) {
        userDTO = userService.getUserByEmail(userDTO.getEmail());
        if (Objects.isNull(userDTO)) {
            return "redirect:/error/error-message?message=Пользователя с данным email не существует!";
        }
        else {
            userService.sendChangePasswordEmail(userDTO);
            return "index";
        }
    }
    
    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 Model model) {
        model.addAttribute("uuid", uuid);
        return "users/changePassword";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 @ModelAttribute("changePasswordForm") UserDTO userDTO) {
        userService.changePassword(uuid, userDTO.getPassword());
        return "redirect:/login";
    }
}
