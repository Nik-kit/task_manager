package com.tereshchenko.taskmanager.service;

import com.tereshchenko.taskmanager.model.Comment;
import com.tereshchenko.taskmanager.model.Task;
import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.repository.DynamicSpecification;
import com.tereshchenko.taskmanager.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
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
    private UserService userService;

    public Task createTask(Task task){

        User currentUser = userService.getCurrentUser();

        task.setAuthor(currentUser);
        task.setCreatedAt(LocalDateTime.now());

        List<Comment> comments = task.getComments();

        if(comments != null && !comments.isEmpty()){

            for(Comment comment : comments){

                if(comment.getId() == null) {

                    comment.setAuthor(currentUser);
                    comment.setTask(task);
                    comment.setCreatedAt(LocalDateTime.now());
                }
            }
        }

        return taskRepository.save(task);
    }

    public Task updateTaskWithRoleChecks(Long id, Task updatedTask){

        Task existingTask = getTaskById(id);

        User currentUser = userService.getCurrentUser();

        List<Comment> comments = updatedTask.getComments();

        if(userService.hasRole(currentUser, "ROLE_ADMIN")){

            if (updatedTask.getExecutor() != null && updatedTask.getExecutor().getId() != null) {

                User executor = userService.getUserById(updatedTask.getExecutor().getId());

                updatedTask.setExecutor(executor);
            }

            BeanUtils.copyProperties(updatedTask, existingTask, "id", "comments");

        } else if(userService.hasRole(currentUser, "ROLE_USER") && isTaskAssignee(id, currentUser.getEmail())) {

            if (updatedTask.getStatus() != null) {

                existingTask.setStatus(updatedTask.getStatus());
            }
        } else {

            throw new RuntimeException("Not enough permissions to update the task");
        }

        if(comments != null && !comments.isEmpty()){

            for(Comment comment : comments){

                if(comment.getId() == null) {

                    comment.setAuthor(currentUser);
                    comment.setTask(existingTask);
                    comment.setCreatedAt(LocalDateTime.now());

                    existingTask.getComments().add(comment);
                }
            }
        }
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id){

        Task task = getTaskById(id);

        taskRepository.delete(task);
    }

    public Page<Task> getFilteredTasks(Task filter, int page, int size){

        if(filter == null){

            filter = new Task();
        }

        User currentUser = userService.getCurrentUser();

        if(userService.hasRole(currentUser, "ROLE_USER")){

            filter.setExecutor(currentUser);
        }

        Pageable pageable = PageRequest.of(page, size);

        Specification<Task> spec = DynamicSpecification.filterByEntity(filter, "comments");

        return taskRepository.findAll(spec, pageable);
    }

    public Page<Task> getTasksByAuthor(Long authorId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        User author = userService.getUserById(authorId);

        return taskRepository.findByAuthor(author, pageable);
    }

    public Page<Task> getTasksByExecutor(Long executorId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        User executor = userService.getUserById(executorId);

        return taskRepository.findByExecutor(executor, pageable);
    }

    public Task getTaskById(Long id){

        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public boolean isTaskAssignee(Long taskId, String username){

        Task task = getTaskById(taskId);

        return task.getExecutor().getUsername().equals(username);
    }
}
