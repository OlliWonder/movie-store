package com.sber.java13.filmlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "order_seq", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Order extends GenericModel {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ORDERS_USER"))
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ORDERS_FILM"))
    private Film film;
    
    @Column(name = "rent_date", nullable = false)
    private LocalDate rentDate;
    
    @Column(name = "rent_period", nullable = false)
    private Integer rentPeriod;
    
    @Column(name = "purchase", nullable = false)
    private Boolean isPurchased;
}
