package com.tereshchenko.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tereshchenko.taskmanager.model.Priority;
import com.tereshchenko.taskmanager.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequestFilterDTO (

    @Schema(description = "ID of the task", example = "3")
    Long id,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Creation date of the task (yyyy-MM-dd)", example = "2025-05-11")
    LocalDate createdAt,

    @Size(max = 255, message = "Task title must not exceed 255 characters")
    @Schema(description = "Title of the task", example = "Fix login bug")
    String title,

    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    @Schema(description = "Updated task description", example = "User still can't log in even after reset")
    String description,

    @Schema(description = "Updated task status", example = "COMPLETED")
    Status status,

    @Schema(description = "Updated task priority", example = "MEDIUM")
    Priority priority,

    @Schema(description = "Updated executor ID", example = "4")
    Long executorId
){}
