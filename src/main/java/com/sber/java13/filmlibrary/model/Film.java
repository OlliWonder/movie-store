package com.sber.java13.filmlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "film_seq", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Film extends GenericModel {
    
    @Column(name = "title", nullable = false)
    private String filmTitle;
    
    @Column(name = "premier_year", nullable = false)
    private Integer premierYear;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "genre")
    @Enumerated
    private Genre genre;
    
    @ManyToMany
    @JoinTable(name = "films_directors",
    joinColumns = @JoinColumn(name = "film_id"), foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
    inverseJoinColumns = @JoinColumn(name = "director_id"), inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Director> directors;
    
    @OneToMany(mappedBy = "film")
    private Set<Order> orders;
}
