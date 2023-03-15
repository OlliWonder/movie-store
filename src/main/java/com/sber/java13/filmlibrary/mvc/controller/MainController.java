package com.sber.java13.filmlibrary.mvc.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class MainController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
