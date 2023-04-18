package com.sber.java13.filmlibrary.dto;

import com.sber.java13.filmlibrary.model.Director;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.model.Genre;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilmDTO extends GenericDTO {
    private String filmTitle;
    private Integer premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
    private Set<Long> ordersIds;
    private boolean isDeleted;
    
    public FilmDTO(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setFilmTitle(film.getFilmTitle());
        filmDTO.setPremierYear(film.getPremierYear());
        filmDTO.setCountry(film.getCountry());
        filmDTO.setGenre(film.getGenre());
        Set<Director> directors = film.getDirectors();
        Set<Long> directorIds = new HashSet<>();
        if (directors != null && directors.size() > 0) {
            directors.forEach(a -> directorIds.add(a.getId()));
        }
        filmDTO.setDirectorsIds(directorIds);
    }
}
