package com.tereshchenko.taskmanager.repository;

import com.tereshchenko.taskmanager.model.Task;
import com.tereshchenko.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByExecutor(User executor, Pageable pageable);

    Page<Task> findByAuthor(User author, Pageable pageable);
}
