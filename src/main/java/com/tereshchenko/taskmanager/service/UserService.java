package com.tereshchenko.taskmanager.service;

import com.tereshchenko.taskmanager.dto.UserRequestCreateDTO;
import com.tereshchenko.taskmanager.dto.UserResponseDTO;
import com.tereshchenko.taskmanager.dto.UserRequestUpdateDTO;
import com.tereshchenko.taskmanager.mapper.UserMapper;
import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestCreateDTO dto){

        User user = userMapper.toEntity(dto);

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserResponseDTO updateUser(Long id, UserRequestUpdateDTO dto){

        User user = getUserById(id);

        userMapper.updateUserFromDTO(dto, user);

        return userMapper.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id){

        User user = getUserById(id);

        userRepository.delete(user);
    }

    public User getUserById(Long id){

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email){

        return userRepository.findByEmail(email);
    }

    public UserResponseDTO getUserDTOByEmail(String email){

        return userMapper.toDTO(userRepository.findByEmail(email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {

            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        return user;
    }

    public User getCurrentUser() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return getUserByEmail(email);
    }

    public boolean hasRole(User user, String role) {

        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
