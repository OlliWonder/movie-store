package com.sber.java13.filmlibrary.dto;

import com.sber.java13.filmlibrary.model.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmWithDirectorsDTO extends FilmDTO {
    private Set<DirectorDTO> directors;
    
    public FilmWithDirectorsDTO(Film film, Set<DirectorDTO> directors) {
        super(film);
        this.directors = directors;
    }
}
