package com.sber.java13.filmlibrary.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO extends GenericDTO {
    private UserDTO user;
    private FilmDTO filmDTO;
    private String rentDate;
    private Integer rentPeriod;
    private Boolean isPurchased;
    private Long filmId;
    private Long userId;
}
