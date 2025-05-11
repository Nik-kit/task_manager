package com.tereshchenko.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO for returning comment details")
public record CommentResponseDTO(

        @Schema(description = "Comment ID", example = "101")
        Long id,

        @Schema(description = "Comment content", example = "Looks good to me.")
        String content,

        @Schema(description = "Time when the comment was created", example = "2025-05-01T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Author of the comment", example = "user@example.com")
        String author
) {}
