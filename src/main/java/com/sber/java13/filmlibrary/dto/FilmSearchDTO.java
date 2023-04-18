package com.sber.java13.filmlibrary.dto;

import com.sber.java13.filmlibrary.model.Genre;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilmSearchDTO {
    private String filmTitle;
    private String directorsFio;
    private Genre genre;
}
