package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.IdFilmDirectorDTO;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.service.DirectorService;
import com.sber.java13.filmlibrary.service.FilmService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static com.sber.java13.filmlibrary.constants.UserRoleConstants.ADMIN;

@Controller
@Hidden
@RequestMapping("directors")
@Slf4j
public class MVCDirectorController {
    private final DirectorService directorService;
    private final FilmService filmService;
    
    public MVCDirectorController(DirectorService directorService, FilmService filmService) {
        this.directorService = directorService;
        this.filmService = filmService;
    }
    
    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorsFio"));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<DirectorDTO> result;
        if (ADMIN.equalsIgnoreCase(userName)) {
            result = directorService.listAll(pageRequest);
        }
        else {
            result = directorService.listAllNotDeleted(pageRequest);
        }
        model.addAttribute("directors", result);
        model.addAttribute("exception", exception);
        return "directors/viewAllDirectors";
    }
    
    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorService.getOne(id));
        return "directors/viewDirector";
    }
    
    @GetMapping("/add")
    public String create() {
        return "directors/addDirector";
    }
    
    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDTO directorDTO) {
        directorService.create(directorDTO);
        return "redirect:/directors";
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorService.getOne(id));
        return "directors/updateDirector";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorDTO directorDTO) {
        directorService.update(directorDTO);
        return "redirect:/directors";
    }
    
    @GetMapping("/add-film/{directorId}")
    public String addFilm(@PathVariable Long directorId, Model model) {
        model.addAttribute("films", filmService.listAll());
        model.addAttribute("directorId", directorId);
        model.addAttribute("director", directorService.getOne(directorId).getDirectorsFio());
        return "directors/addDirectorFilm";
    }
    
    @PostMapping("/add-film")
    public String addFilm(@ModelAttribute("directorFilmForm") IdFilmDirectorDTO idFilmDirectorDTO) {
        directorService.addFilm(idFilmDirectorDTO);
        return "redirect:/directors";
    }
    
    @PostMapping("/search")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("directorSearchForm") DirectorDTO directorDTO, Model model) {
        if (StringUtils.hasText(directorDTO.getDirectorsFio()) || StringUtils.hasLength(directorDTO.getDirectorsFio())) {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorsFio"));
            model.addAttribute("directors", directorService.searchDirectors(directorDTO.getDirectorsFio().trim(), pageRequest));
            return "directors/viewAllDirectors";
        }
        else {
            return "redirect:/directors";
        }
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        directorService.delete(id);
        return "redirect:/directors";
    }
    
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        directorService.restore(id);
        return "redirect:/directors";
    }
    
    @ExceptionHandler(MyDeleteException.class)
    public RedirectView handleError(HttpServletRequest request, Exception exception, RedirectAttributes redirectAttributes) {
        log.error("Запрос: " + request.getRequestURL() + " вызвал ошибку " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return new RedirectView("/directors", true);
    }
}
