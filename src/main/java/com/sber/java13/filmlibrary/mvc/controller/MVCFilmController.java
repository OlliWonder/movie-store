package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.FilmWithDirectorsDTO;
import com.sber.java13.filmlibrary.dto.IdFilmDirectorDTO;
import com.sber.java13.filmlibrary.service.DirectorService;
import com.sber.java13.filmlibrary.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("films")
public class MVCFilmController {
    private final FilmService filmService;
    private final DirectorService directorService;
    
    public MVCFilmController(FilmService filmService, DirectorService directorService) {
        this.filmService = filmService;
        this.directorService = directorService;
    }
    
    @GetMapping("")
    public String getAll(Model model) {
        List<FilmWithDirectorsDTO> filmWithDirectorsDTOList = filmService.getAllFilmsWithDirectors();
        model.addAttribute("films", filmWithDirectorsDTOList);
        return "films/viewAllFilms";
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
