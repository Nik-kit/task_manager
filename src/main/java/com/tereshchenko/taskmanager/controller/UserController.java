package com.tereshchenko.taskmanager.controller;

import com.tereshchenko.taskmanager.dto.UserRequestCreateDTO;
import com.tereshchenko.taskmanager.dto.UserResponseDTO;
import com.tereshchenko.taskmanager.dto.UserRequestUpdateDTO;
import com.tereshchenko.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Controller", description = "CRUD operations for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user",
            description = "Creates a new user. Required: email, password. Optional: role.")
    @PostMapping
    public UserResponseDTO createUser(@RequestBody @Valid UserRequestCreateDTO dto){

        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user",
            description = "Updates user information by user ID. Optional: role, password.")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestUpdateDTO dto){

        return userService.updateUser(id, dto);
    }

    @Operation(summary = "Delete a user", description = "Deletes the user with the specified ID")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){

        userService.deleteUser(id);
    }

    @Operation(summary = "Get user by email", description = "Returns user details by email address")
    @GetMapping("/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email){

        return userService.getUserDTOByEmail(email);
    }

}
