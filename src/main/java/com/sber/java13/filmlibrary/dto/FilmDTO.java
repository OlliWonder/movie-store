package com.sber.java13.filmlibrary.dto;

import com.sber.java13.filmlibrary.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDTO extends GenericDTO {
    private String filmTitle;
    private Integer premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
    private Set<Long> ordersIds;
    private boolean isDeleted;
}
