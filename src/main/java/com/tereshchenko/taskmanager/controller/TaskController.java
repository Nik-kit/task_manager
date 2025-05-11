package com.tereshchenko.taskmanager.controller;

import com.tereshchenko.taskmanager.dto.*;
import com.tereshchenko.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Controller", description = "CRUD operations and filters for managing tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new task",
            description = "Creates a new task. Required: title, description. Optional: status, priority, executorId, comments.")
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestCreateDTO dto){

        return taskService.createTask(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @taskService.isTaskAssignee(#id, authentication.name))")
    @Operation(summary = "Update a task",
            description = "Updates task fields by ID. Accessible to ADMIN or the assigned USER. Optional fields: title, description, status, priority, executorId, comments.")
    public TaskResponseDTO updateTask(@PathVariable Long id,@Valid @RequestBody TaskRequestUpdateDTO dto){

        return taskService.updateTaskWithRoleChecks(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a task", description = "Deletes a task by ID. Only accessible to ADMIN.")
    public void deleteTask(@PathVariable Long id){

        taskService.deleteTask(id);
    }

    @PostMapping("/filter")
    @Operation(summary = "Filter tasks",
            description = "Returns tasks based on optional filter criteria(createdAt, title, description, status, priority, author, executor). Supports pagination. Accessible to ADMIN or the assigned USER.")
    public PageResponseDTO<TaskResponseDTO> getFilteredTasks(@RequestBody(required = false) TaskRequestFilterDTO filter,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size){

        return taskService.getFilteredTasks(filter, page, size);
    }

    @GetMapping("/executor/{executorId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #executorId == principal.id)")
    @Operation(summary = "Get tasks by executor",
            description = "Returns a page of tasks by executor ID. Accessible to ADMIN or the executor.")
    public PageResponseDTO<TaskResponseDTO> getTasksByExecutor(@PathVariable Long executorId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size){

        return taskService.getTasksByExecutor(executorId, page, size);
    }

    @GetMapping("/author/{authorId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get tasks by author",
            description = "Returns a page of tasks by author ID. Accessible to ADMIN or to the USER if they are the executor of these tasks.")
    public PageResponseDTO<TaskResponseDTO> getTasksByAuthor(@PathVariable Long authorId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size){

        return taskService.getTasksByAuthor(authorId, page, size);
    }
}
