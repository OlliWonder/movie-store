package com.sber.java13.filmlibrary.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends GenericDTO {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String phone;
    private String address;
    private String email;
    private LocalDate createdWhen;
    private RoleDTO role;
    private Set<Long> ordersIds;
    private String changePasswordToken;
}
