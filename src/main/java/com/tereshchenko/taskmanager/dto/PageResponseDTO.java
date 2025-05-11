package com.tereshchenko.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Generic paginated response DTO")
public record PageResponseDTO<T>(

    @Schema(description = "List of elements in the current page")
    List<T> content,

    @Schema(description = "Current page number (0-based)", example = "0")
    int page,

    @Schema(description = "Number of elements per page", example = "10")
    int size,

    @Schema(description = "Total number of pages", example = "5")
    int totalPages,

    @Schema(description = "Total number of elements", example = "42")
    long totalElements,

    @Schema(description = "Indicates if this is the first page", example = "true")
    boolean first,

    @Schema(description = "Indicates if this is the last page", example = "false")
    boolean last
) {
}
