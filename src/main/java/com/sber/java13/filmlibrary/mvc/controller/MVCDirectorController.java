package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.DirectorWithFilmsDTO;
import com.sber.java13.filmlibrary.service.DirectorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("directors")
public class MVCDirectorController {
    private final DirectorService directorService;
    
    public MVCDirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }
    
    @GetMapping("")
    public String getAll(Model model) {
        List<DirectorWithFilmsDTO> directorWithFilmsDTOList = directorService.getAllDirectorsWithFilms();
        model.addAttribute("directors", directorWithFilmsDTOList);
        return "directors/viewAllDirectors";
    }
    
    @GetMapping("/add")
    public String create() {
        return "directors/addDirector";
    }
    
    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm")DirectorDTO directorDTO) {
        directorService.create(directorDTO);
        return "redirect:/directors";
    }
}
