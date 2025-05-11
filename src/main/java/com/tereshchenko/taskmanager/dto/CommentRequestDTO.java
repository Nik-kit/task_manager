package com.tereshchenko.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a new comment")
public record CommentRequestDTO(

        @NotBlank(message = "Comment should not be empty")
        @Size(max = 500, message = "Comment should not exceed 500 characters")
        @Schema(description = "Comment text", example = "This is a comment", maxLength = 500)
        String content
) {}
