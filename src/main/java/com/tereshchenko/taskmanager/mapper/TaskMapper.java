package com.tereshchenko.taskmanager.mapper;

import com.tereshchenko.taskmanager.dto.TaskRequestCreateDTO;
import com.tereshchenko.taskmanager.dto.TaskRequestFilterDTO;
import com.tereshchenko.taskmanager.dto.TaskRequestUpdateDTO;
import com.tereshchenko.taskmanager.dto.TaskResponseDTO;
import com.tereshchenko.taskmanager.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {MapperUtils.class,
                CommentMapper.class})
public interface TaskMapper {

    @Mapping(source = "author.email", target = "author")
    @Mapping(source = "executor.email", target = "executor")
    @BeanMapping(ignoreUnmappedSourceProperties = {"author", "executor"})
    TaskResponseDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task toEntity(TaskRequestCreateDTO dto);

    @Mapping(target = "comments", ignore = true)
    Task toEntity(TaskRequestFilterDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "title", qualifiedByName = "isNotBlank")
    @Mapping(target = "description", qualifiedByName = "isNotBlank")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateTaskFromDTO(TaskRequestUpdateDTO dto, @MappingTarget Task task);
}
