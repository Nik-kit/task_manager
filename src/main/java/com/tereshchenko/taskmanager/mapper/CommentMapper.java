package com.tereshchenko.taskmanager.mapper;

import com.tereshchenko.taskmanager.dto.CommentResponseDTO;
import com.tereshchenko.taskmanager.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author.email", target = "author")
    CommentResponseDTO toDTO(Comment comment);
}
