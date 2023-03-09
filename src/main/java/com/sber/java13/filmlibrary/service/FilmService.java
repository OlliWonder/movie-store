package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.FilmWithDirectorsDTO;
import com.sber.java13.filmlibrary.mapper.FilmMapper;
import com.sber.java13.filmlibrary.mapper.FilmWithDirectorsMapper;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService extends GenericService<Film, FilmDTO> {
    private final FilmRepository filmRepository;
    private final FilmWithDirectorsMapper filmWithDirectorsMapper;
    
    protected FilmService(FilmRepository filmRepository, FilmMapper filmMapper,
                          FilmWithDirectorsMapper filmWithDirectorsMapper) {
        super(filmRepository, filmMapper);
        this.filmRepository = filmRepository;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
    }
    
    public List<FilmWithDirectorsDTO> getAllFilmsWithDirectors() {
        return filmWithDirectorsMapper.toDTOs(filmRepository.findAll());
    }
}
