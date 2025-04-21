package com.tereshchenko.taskmanager.service;

import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    public User createUser(User user){

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser){

        User user = getUserById(id);

        BeanUtils.copyProperties(updatedUser, user);

        return userRepository.save(user);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
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
