package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.service.DirectorService;
import com.sber.java13.filmlibrary.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
public class FilmController extends GenericController<Film, FilmDTO> {
    
    private final FilmService filmService;
    private final DirectorService directorService;
    
    public FilmController(FilmService filmService, DirectorService directorService) {
        super(filmService);
        this.filmService = filmService;
        this.directorService = directorService;
    }
    
    @Operation(description = "Добавить режиссёра к фильму", method = "addDirector")
    @RequestMapping(value = "/addDirector", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDTO> addDirector(@RequestParam(value = "filmId") Long filmId,
                                          @RequestParam(value = "directorId") Long directorId) {
        FilmDTO filmDTO = filmService.getOne(filmId);
        DirectorDTO directorDTO = directorService.getOne(directorId);
        filmDTO.getDirectorsIds().add(directorDTO.getId());
        directorDTO.getFilmsIds().add(filmDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(filmService.update(filmDTO));
    }
}
