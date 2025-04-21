package com.tereshchenko.taskmanager.controller;

import com.tereshchenko.taskmanager.model.Task;
import com.tereshchenko.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Task createTask(@RequestBody Task task){

        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @taskService.isTaskAssignee(#id, authentication.name))")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask){

        return taskService.updateTaskWithRoleChecks(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTask(@PathVariable Long id){

        taskService.deleteTask(id);
    }

    @PostMapping("/filter")
    public Page<Task> getFilteredTasks(@RequestBody(required = false) Task filter,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size){

        return taskService.getFilteredTasks(filter, page, size);
    }

    @GetMapping("/executor/{executorId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #executorId == principal.id)")
    public Page<Task> getTasksByExecutor(@PathVariable Long executorId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size){

        return taskService.getTasksByExecutor(executorId, page, size);
    }

    @GetMapping("/author/{authorId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #authorId == principal.id)")
    public Page<Task> getTasksByAuthor(@PathVariable Long authorId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size){

        return taskService.getTasksByAuthor(authorId, page, size);
    }
}
