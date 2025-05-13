package com.tereshchenko.taskmanager.repository;

import com.tereshchenko.taskmanager.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
