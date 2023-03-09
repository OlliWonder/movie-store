package com.sber.java13.filmlibrary.model;

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
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_directors",
    joinColumns = @JoinColumn(name = "film_id"), foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
    inverseJoinColumns = @JoinColumn(name = "director_id"), inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    private Set<Director> directors;
    
    @OneToMany(mappedBy = "film", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Order> orders;
}
