package com.app.MediQuirk.config;

import com.app.MediQuirk.model.Role;
import com.app.MediQuirk.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class InitialDataLoader {

    @Bean
    public CommandLineRunner initializeRoles(IRoleRepository roleRepository) {
        return args -> {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
            if (adminRoleOptional.isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                adminRole.setDescription("Admin role");
                roleRepository.save(adminRole);
            }

            Optional<Role> userRoleOptional = roleRepository.findByName("USER");
            if (userRoleOptional.isEmpty()) {
                Role userRole = new Role();
                userRole.setName("USER");
                userRole.setDescription("User role");
                roleRepository.save(userRole);
            }
        };
    }
}
