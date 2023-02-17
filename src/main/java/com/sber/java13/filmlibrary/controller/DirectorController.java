package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.model.Director;
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
@RequestMapping("/directors")
@Tag(name = "Режиссёры", description = "Контроллер для работы с режиссёрами фильмов")
public class DirectorController extends GenericController<Director, DirectorDTO> {
    
    private final DirectorService directorService;
    private final FilmService filmService;
    
    public DirectorController(DirectorService directorService, FilmService filmService) {
        super(directorService);
        this.directorService = directorService;
        this.filmService = filmService;
    }
    
    @Operation(description = "Добавить фильм к режиссёру", method = "addFilm")
    @RequestMapping(value = "/addFilm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> addFilm(@RequestParam(value = "filmId") Long filmId,
                                                @RequestParam(value = "directorId") Long directorId) {
        DirectorDTO directorDTO = directorService.getOne(directorId);
        FilmDTO filmDTO = filmService.getOne(filmId);
        directorDTO.getFilmsIds().add(filmDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(directorService.update(directorDTO));
    }
}
