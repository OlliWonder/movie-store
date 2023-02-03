package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.model.Director;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.repository.DirectorRepository;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/directors")
@Tag(name = "Режиссёры", description = "Контроллер для работы с режиссёрами фильмов")
public class DirectorController extends GenericController<Director> {
    
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    
    public DirectorController(DirectorRepository directorRepository, FilmRepository filmRepository) {
        super(directorRepository);
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
    }
    
    @Operation(description = "Добавить фильм к режиссёру", method = "addFilm")
    @RequestMapping(value = "/addFilm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Director> addFilm(@RequestParam(value = "filmId") Long filmId,
                                                @RequestParam(value = "directorId") Long directorId) {
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссёр с ID: " + directorId + " не найден"));
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID: " + filmId + " не найден"));
        director.getFilms().add(film);
        return ResponseEntity.status(HttpStatus.OK).body(directorRepository.save(director));
    }
}
