package com.tereshchenko.taskmanager.service;

import com.tereshchenko.taskmanager.dto.*;
import com.tereshchenko.taskmanager.mapper.TaskMapper;
import com.tereshchenko.taskmanager.model.Comment;
import com.tereshchenko.taskmanager.model.Task;
import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.repository.DynamicSpecification;
import com.tereshchenko.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserService userService;

    public TaskResponseDTO createTask(TaskRequestCreateDTO dto){

        Task task = taskMapper.toEntity(dto);

        User currentUser = userService.getCurrentUser();

        task.setAuthor(currentUser);
        task.setCreatedAt(LocalDateTime.now());

        List<Comment> comments = mapComments(dto.comments(), currentUser, task);

        task.setComments(comments);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    public TaskResponseDTO updateTaskWithRoleChecks(Long id, TaskRequestUpdateDTO dto){

        Task existingTask = getTaskById(id);

        User currentUser = userService.getCurrentUser();

        if(userService.hasRole(currentUser, "ROLE_ADMIN")){

            taskMapper.updateTaskFromDTO(dto, existingTask);

        } else if(userService.hasRole(currentUser, "ROLE_USER") && isTaskAssignee(id, currentUser.getEmail())) {

            if (dto.status() != null) {

                existingTask.setStatus(dto.status());
            }
        } else {

            throw new RuntimeException("Not enough permissions to update the task");
        }

        List<Comment> mapComments = mapComments(dto.comments(), currentUser, existingTask);

        existingTask.getComments().addAll(mapComments);

        return taskMapper.toDTO(taskRepository.save(existingTask));
    }

    public void deleteTask(Long id){

        Task task = getTaskById(id);

        taskRepository.delete(task);
    }

    public PageResponseDTO<TaskResponseDTO> getFilteredTasks(TaskRequestFilterDTO filterDTO, int page, int size){

        Task filter = taskMapper.toEntity(filterDTO);

        if(filter == null){

            filter = new Task();
        }

        User currentUser = userService.getCurrentUser();

        if(userService.hasRole(currentUser, "ROLE_USER")){

            filter.setExecutor(currentUser);
        }

        Pageable pageable = PageRequest.of(page, size);

        Specification<Task> spec = DynamicSpecification.filterByEntity(filter, "comments");

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return toPageResponseDTO(taskPage);
    }

    public PageResponseDTO<TaskResponseDTO> getTasksByAuthor(Long authorId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        User author = userService.getUserById(authorId);

        Page<Task> taskPage = taskRepository.findByAuthor(author, pageable);

        return toPageResponseDTO(taskPage);
    }

    public PageResponseDTO<TaskResponseDTO> getTasksByExecutor(Long executorId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        User executor = userService.getUserById(executorId);

        Page<Task> taskPage = taskRepository.findByExecutor(executor, pageable);

        return toPageResponseDTO(taskPage);
    }

    public Task getTaskById(Long id){

        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public boolean isTaskAssignee(Long taskId, String username){

        Task task = getTaskById(taskId);

        return task.getExecutor().getUsername().equals(username);
    }

    private PageResponseDTO<TaskResponseDTO> toPageResponseDTO(Page<Task> taskPage) {

        List<TaskResponseDTO> dtoList = taskPage.getContent().stream()
                .map(taskMapper::toDTO)
                .toList();

        return new PageResponseDTO<>(
                dtoList,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalPages(),
                taskPage.getTotalElements(),
                taskPage.isFirst(),
                taskPage.isLast()
        );
    }

    private List<Comment> mapComments(List<CommentRequestDTO> commentsDTO, User author, Task task) {

        if (commentsDTO == null || commentsDTO.isEmpty()) return List.of();

        return commentsDTO.stream().map(commentDTO -> {

            Comment comment = new Comment();

            comment.setAuthor(author);
            comment.setTask(task);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setContent(commentDTO.content());

            return comment;

        }).toList();
    }
}
