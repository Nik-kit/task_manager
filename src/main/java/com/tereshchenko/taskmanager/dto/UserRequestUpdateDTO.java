package com.tereshchenko.taskmanager.dto;

import com.tereshchenko.taskmanager.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for updating an existing user")
public record UserRequestUpdateDTO(

    @Size(min = 6, message = "Password must contain at least 6 characters")
    @Schema(description = "New password for the user", example = "newPassword456")
    String password,

    @Schema(description = "Updated role of the user", example = "USER")
    Role role
){}
