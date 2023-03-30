package com.sber.java13.filmlibrary.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DirectorDTO extends GenericDTO {
    private String directorsFio;
    private String directorsPosition;
    private Set<Long> filmsIds;
    private boolean isDeleted;
}
