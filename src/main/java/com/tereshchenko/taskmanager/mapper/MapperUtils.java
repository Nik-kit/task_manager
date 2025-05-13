package com.tereshchenko.taskmanager.mapper;

import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.service.UserService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    @Autowired
    private UserService userService;

    @Named("isNotBlank")
    public static boolean isNotBlank(String value) {

        return value != null && !value.isBlank();
    }

    @Named("mapIdToUser")
    public User mapIdToUser(Long id) {

        if (id == null) return null;

        return userService.getUserById(id);
    }


}
