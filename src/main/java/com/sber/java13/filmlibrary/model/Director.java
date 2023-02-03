package com.sber.java13.filmlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Director extends GenericModel {
    
    @Column(name = "directors_fio", nullable = false)
    private String directorsFio;
    
    @Column(name = "position", nullable = false)
    private String directorsPosition;
    
    @ManyToMany
    @JoinTable(name = "films_directors",
    joinColumns = @JoinColumn(name = "director_id"), foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
    inverseJoinColumns = @JoinColumn(name = "film_id"), inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"))
    private Set<Film> films;
}
