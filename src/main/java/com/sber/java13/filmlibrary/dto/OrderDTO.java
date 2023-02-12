package com.sber.java13.filmlibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO extends GenericDTO {
    private UserDTO user;
    private FilmDTO film;
    private LocalDate rentDate;
    private Integer rentPeriod;
    private Boolean isPurchased;
    private Long filmId;
    private Long userId;
}
