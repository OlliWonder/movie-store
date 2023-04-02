package com.sber.java13.filmlibrary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "director_seq", allocationSize = 1)
public class Director extends GenericModel {
    
    @Column(name = "directors_fio", nullable = false)
    private String directorsFio;
    
    @Column(name = "position", nullable = false)
    private String directorsPosition;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_directors",
    joinColumns = @JoinColumn(name = "director_id"), foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
    inverseJoinColumns = @JoinColumn(name = "film_id"), inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"))
    private Set<Film> films;
}
