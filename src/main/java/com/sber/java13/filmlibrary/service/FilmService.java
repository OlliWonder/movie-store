package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.mapper.FilmMapper;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import org.springframework.stereotype.Service;

@Service
public class FilmService extends GenericService<Film, FilmDTO> {
    
    protected FilmService(FilmRepository filmRepository, FilmMapper filmMapper) {
        super(filmRepository, filmMapper);
    }
}
