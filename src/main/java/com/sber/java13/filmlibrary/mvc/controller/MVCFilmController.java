package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.*;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.service.DirectorService;
import com.sber.java13.filmlibrary.service.FilmService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Hidden
@RequestMapping("films")
@Slf4j
public class MVCFilmController {
    private final FilmService filmService;
    private final DirectorService directorService;
    
    public MVCFilmController(FilmService filmService, DirectorService directorService) {
        this.filmService = filmService;
        this.directorService = directorService;
    }
    
    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "filmTitle"));
        Page<FilmWithDirectorsDTO> result = filmService.getAllFilmsWithDirectors(pageRequest);
        model.addAttribute("films", result);
        return "films/viewAllFilms";
    }
    
    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmService.getFilmWithDirectors(id));
        return "films/viewFilm";
    }
    
    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }
    
    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmDTO filmDTO) {
        filmService.create(filmDTO);
        return "redirect:/films";
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmService.getOne(id));
        return "films/updateFilm";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute("filmForm") FilmDTO filmDTO) {
        filmService.update(filmDTO);
        return "redirect:/films";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            filmService.delete(id);
        }
        catch (MyDeleteException e) {
            log.error("MVCFilmController#delete(): {}", e.getMessage());
            return "redirect:/error/error-message?message=" + e.getLocalizedMessage();
        }
        return "redirect:/films";
    }
    
    @PostMapping("/search")
    public String searchBooks(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("filmSearchForm") FilmSearchDTO filmSearchDTO,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("films", filmService.findFilms(filmSearchDTO, pageRequest));
        return "films/viewAllFilms";
    }
    
    @GetMapping("/addDirector/{id}")
    public String addDirector(@PathVariable Long id, Model model) {
        List<DirectorDTO> options = directorService.listAll();
        model.addAttribute("options", options);
        model.addAttribute("film", filmService.getOne(id));
        return "films/addDirectorToFilm";
    }
    
    @PostMapping("/addDirector")
    public String addDirector(@ModelAttribute("idFilmDirectorDTO") @Valid IdFilmDirectorDTO idFilmDirectorDTO) {
        Long filmId = idFilmDirectorDTO.getFilmId();
        Long directorId = idFilmDirectorDTO.getDirectorId();
        FilmDTO filmDTO = filmService.getOne(filmId);
        DirectorDTO directorDTO = directorService.getOne(directorId);
        if (!filmDTO.getDirectorsIds().contains(directorId)) {
            filmDTO.getDirectorsIds().add(directorDTO.getId());
            filmService.update(filmDTO);
        }
        return "redirect:/films";
    }
}
