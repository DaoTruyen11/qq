package com.app.MediQuirk.config;

import com.app.MediQuirk.model.Role;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.repository.IRoleRepository;
import com.app.MediQuirk.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Tìm vai trò ADMIN, nếu không tồn tại thì tạo mới
                Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
                Role adminRole;
                if (adminRoleOptional.isEmpty()) {
                    adminRole = new Role();
                    adminRole.setName("ADMIN");
                    adminRole.setDescription("Admin role");
                    roleRepository.save(adminRole);
                } else {
                    adminRole = adminRoleOptional.get();
                }

                // Tạo người dùng admin
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("{bcrypt}$2a$10$cJT14ctLKdeRFTxYaBohTekV1OUpjmFTHMXF3XpEMdSg2qHhs/Vsa"));

                // Thiết lập vai trò ADMIN cho người dùng admin
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);

                userRepository.save(admin);
            }
        };
    }
}
