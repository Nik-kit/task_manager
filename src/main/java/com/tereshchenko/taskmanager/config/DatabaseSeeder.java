package com.tereshchenko.taskmanager.config;

import com.tereshchenko.taskmanager.model.Role;
import com.tereshchenko.taskmanager.model.User;
import com.tereshchenko.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() == 0){

            User admin = new User();

            admin.setEmail("admin@mail.com");
            admin.setPassword("admin");
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Admin created: admin@mail.com / admin");
        }
    }
}
