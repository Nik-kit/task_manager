package com.tereshchenko.taskmanager.mapper;

import com.tereshchenko.taskmanager.dto.UserRequestCreateDTO;
import com.tereshchenko.taskmanager.dto.UserResponseDTO;
import com.tereshchenko.taskmanager.dto.UserRequestUpdateDTO;
import com.tereshchenko.taskmanager.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = MapperUtils.class)
public interface UserMapper {

    UserResponseDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserRequestCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", qualifiedByName = "isNotBlank")
    @Mapping(target = "authorities", ignore = true)
    void updateUserFromDTO(UserRequestUpdateDTO dto, @MappingTarget User user);
}
