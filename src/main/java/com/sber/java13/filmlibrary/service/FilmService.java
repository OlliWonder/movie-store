package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.constants.Errors;
import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.FilmSearchDTO;
import com.sber.java13.filmlibrary.dto.FilmWithDirectorsDTO;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.mapper.FilmMapper;
import com.sber.java13.filmlibrary.mapper.FilmWithDirectorsMapper;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@Slf4j
public class FilmService extends GenericService<Film, FilmDTO> {
    private final FilmRepository filmRepository;
    private final FilmWithDirectorsMapper filmWithDirectorsMapper;
    
    protected FilmService(FilmRepository filmRepository, FilmMapper filmMapper,
                          FilmWithDirectorsMapper filmWithDirectorsMapper) {
        super(filmRepository, filmMapper);
        this.filmRepository = filmRepository;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
    }
    
    public Page<FilmWithDirectorsDTO> getAllFilmsWithDirectors(Pageable pageable) {
        Page<Film> filmsPaginated = repository.findAll(pageable);
        List<FilmWithDirectorsDTO> result = filmWithDirectorsMapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageable, filmsPaginated.getTotalElements());
    }
    
    public Page<FilmWithDirectorsDTO> getAllNotDeletedFilmsWithDirectors(Pageable pageable) {
        Page<Film> filmsPaginated = filmRepository.findAllByIsDeletedFalse(pageable);
        List<FilmWithDirectorsDTO> result = filmWithDirectorsMapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageable, filmsPaginated.getTotalElements());
    }
    
    public FilmWithDirectorsDTO getFilmWithDirectors(Long id) {
        return filmWithDirectorsMapper.toDTO(mapper.toEntity(super.getOne(id)));
    }
    
    public Page<FilmWithDirectorsDTO> findFilms(FilmSearchDTO filmSearchDTO, Pageable pageable) {
        String genre = filmSearchDTO.getGenre() != null ? String.valueOf(filmSearchDTO.getGenre().ordinal()) : null;
        Page<Film> filmsPaginated = filmRepository.searchFilms(genre,
                filmSearchDTO.getFilmTitle(),
                filmSearchDTO.getDirectorsFio(),
                pageable
        );
        List<FilmWithDirectorsDTO> result = filmWithDirectorsMapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageable, filmsPaginated.getTotalElements());
    }
    
    public FilmDTO create(final FilmDTO object) {
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    public FilmDTO update(final FilmDTO object) {
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    @Override
    public void delete(Long id) throws MyDeleteException {
        Film film = filmRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Фильма с заданным ID=" + id + " не существует"));
        boolean filmCanBeDeleted = filmRepository.checkFilmForDeletion(id);
        if (filmCanBeDeleted) {
            markAsDeleted(film);
            filmRepository.save(film);
        }
        else {
            throw new MyDeleteException(Errors.Films.FILM_DELETE_ERROR);
        }
    }
    
    public void restore(Long objectId) {
        Film film = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Фильма с заданным ID=" + objectId + " не существует"));
        unMarkAsDeleted(film);
        repository.save(film);
    }
}
