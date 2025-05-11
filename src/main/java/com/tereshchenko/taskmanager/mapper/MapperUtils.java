package com.tereshchenko.taskmanager.mapper;

import com.tereshchenko.taskmanager.model.User;
import org.mapstruct.Condition;
import org.mapstruct.Named;

public class MapperUtils {

    @Named("isNotBlank")
    public static boolean isNotBlank(String value) {

        return value != null && !value.isBlank();
    }
}
