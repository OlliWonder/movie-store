package com.sber.java13.filmlibrary.mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MVCErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest) {
        log.error("ОЙ! Ошибка {}", httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return "error";
    }
    
    @RequestMapping("/error/error-message")
    public String handleError(@RequestParam(value = "message") String message, Model model) {
        model.addAttribute("message", message);
        return "error";
    }
}
