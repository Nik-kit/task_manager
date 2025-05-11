package com.tereshchenko.taskmanager.dto;

import com.tereshchenko.taskmanager.model.Priority;
import com.tereshchenko.taskmanager.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO for creating a new task")
public record TaskRequestCreateDTO(

        @NotBlank(message = "Task header should not be empty")
        @Size(max = 255, message = "Task title must not exceed 255 characters")
        @Schema(description = "Title of the task", example = "Fix login bug")
        String title,

        @NotBlank(message = "Task description should not be empty")
        @Size(max = 1000, message = "Description should not exceed 1000 characters")
        @Schema(description = "Detailed description of the task", example = "User can't log in after password reset")
        String description,

        @Schema(description = "Status of the task. Optional", example = "IN_PROGRESS")
        Status status,

        @Schema(description = "Priority of the task. Optional", example = "HIGH")
        Priority priority,

        @Schema(description = "ID of the executor (User). Optional", example = "3")
        Long executorId,

        @Valid
        List<CommentRequestDTO> comments
) {}
