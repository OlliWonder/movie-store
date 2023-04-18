package com.sber.java13.filmlibrary.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO extends GenericDTO {
    private UserDTO user;
    private FilmDTO filmDTO;
    private LocalDate rentDate;
    private Integer rentPeriod;
    private Boolean isPurchased;
    private Long filmId;
    private Long userId;
}
