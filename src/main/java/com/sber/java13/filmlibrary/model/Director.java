package com.sber.java13.filmlibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "director_seq", allocationSize = 1)
public class Director extends GenericModel {
    
    @Column(name = "directors_fio", nullable = false)
    private String directorsFio;
    
    @Column(name = "position", nullable = false)
    private String directorsPosition;
    
    @ManyToMany(mappedBy = "directors")
    private Set<Film> films;
}
