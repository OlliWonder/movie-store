package com.sber.java13.filmlibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO extends GenericDTO {
    private String directorsFio;
    private String directorsPosition;
    private Set<Long> filmsIds;
}
