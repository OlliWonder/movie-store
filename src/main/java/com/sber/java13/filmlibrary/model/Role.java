package com.sber.java13.filmlibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "role_seq", allocationSize = 1)
public class Role extends GenericModel {

    @Column(name = "title", nullable = false)
    private String roleTitle;
    
    @Column(name = "description")
    private String roleDescription;
    
    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
