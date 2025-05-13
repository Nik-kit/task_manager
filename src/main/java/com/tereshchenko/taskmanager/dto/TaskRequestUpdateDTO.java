package com.tereshchenko.taskmanager.dto;

import com.tereshchenko.taskmanager.model.Priority;
import com.tereshchenko.taskmanager.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO for updating an existing task")
public record TaskRequestUpdateDTO(

        @Size(max = 255, message = "Task title must not exceed 255 characters")
        @Schema(description = "Updated title of the task", example = "Fix login bug (urgent)")
        String title,

        @Size(max = 1000, message = "Description should not exceed 1000 characters")
        @Schema(description = "Updated task description", example = "User still can't log in even after reset")
        String description,

        @Schema(description = "Updated task status", example = "COMPLETED")
        Status status,

        @Schema(description = "Updated task priority", example = "MEDIUM")
        Priority priority,

        @Schema(description = "Updated executor ID", example = "4")
        Long executorId,

        @Valid
        List<CommentRequestDTO> comments
) {}
