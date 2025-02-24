package com.tereshchenko.taskmanager.entity;

import com.tereshchenko.taskmanager.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Email
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
