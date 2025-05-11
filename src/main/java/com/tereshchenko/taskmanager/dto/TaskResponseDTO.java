package com.tereshchenko.taskmanager.dto;

import com.tereshchenko.taskmanager.model.Priority;
import com.tereshchenko.taskmanager.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO for returning task details")
public record TaskResponseDTO(

        @Schema(description = "ID of the task", example = "42")
        Long id,

        @Schema(description = "Task title", example = "Fix login bug")
        String title,

        @Schema(description = "Task description", example = "User can't log in after password reset")
        String description,

        @Schema(description = "Task status", example = "IN_PROGRESS")
        Status status,

        @Schema(description = "Task priority", example = "HIGH")
        Priority priority,

        @Schema(description = "Timestamp of task creation", example = "2024-04-30T14:12:00")
        LocalDateTime createdAt,

        @Schema(description = "Email of the task author", example = "admin@example.com")
        String author,

        @Schema(description = "Email of the task executor", example = "user@example.com")
        String executor,

        List<CommentResponseDTO> comments
) {}
