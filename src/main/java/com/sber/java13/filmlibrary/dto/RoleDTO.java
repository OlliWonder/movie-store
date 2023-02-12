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
public class RoleDTO extends GenericDTO {
    private String roleTitle;
    private String roleDescription;
    private Set<Long> usersIds;
}
