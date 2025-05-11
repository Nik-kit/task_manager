package com.tereshchenko.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for user response")
public record UserResponseDTO(

        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "Email address of the user", example = "user@example.com")
        String email,

        @Schema(description = "Role of the user", example = "USER")
        String role
){}
