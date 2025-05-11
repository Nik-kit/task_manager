package com.tereshchenko.taskmanager.dto;

import com.tereshchenko.taskmanager.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a new user")
public record UserRequestCreateDTO(

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email")
    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    String email,

    @NotBlank(message = "Password should not be empty")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    @Schema(description = "Password for the user account", example = "strongPassword123", required = true)
    String password,

    @Schema(description = "Role of the user. Optional", example = "USER")
    Role role
){}
