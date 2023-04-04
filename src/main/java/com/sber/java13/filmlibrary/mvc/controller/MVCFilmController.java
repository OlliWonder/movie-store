package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.*;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.service.DirectorService;
import com.sber.java13.filmlibrary.service.FilmService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.sber.java13.filmlibrary.constants.UserRoleConstants.ADMIN;

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
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "filmTitle"));
        Page<FilmWithDirectorsDTO> result;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(userName)) {
            result = filmService.getAllFilmsWithDirectors(pageRequest);
        } else {
            result = filmService.getAllNotDeletedFilmsWithDirectors(pageRequest);
        }
        model.addAttribute("films", result);
        model.addAttribute("exception", exception);
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
    
    @PostMapping("/search")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("filmSearchForm") FilmSearchDTO filmSearchDTO,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("films", filmService.findFilms(filmSearchDTO, pageRequest));
        return "films/viewAllFilms";
    }
    
    @PostMapping("/search/director")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("directorSearchForm") DirectorDTO directorDTO,
                              Model model) {
        FilmSearchDTO filmSearchDTO = new FilmSearchDTO();
        filmSearchDTO.setDirectorsFio(directorDTO.getDirectorsFio());
        filmSearchDTO.setFilmTitle("");
        return searchFilms(page, pageSize, filmSearchDTO, model);
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
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        filmService.delete(id);
        return "redirect:/films";
    }
    
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        filmService.restore(id);
        return "redirect:/films";
    }
    
    @ExceptionHandler(MyDeleteException.class)
    public RedirectView handleError(HttpServletRequest request, Exception exception,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос: " + request.getRequestURL() + " вызвал ошибку " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return new RedirectView("/films", true);
    }
}
