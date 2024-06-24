package com.app.MediQuirk.services;

import com.app.MediQuirk.model.UserProfile;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.repository.UserProfileRepository;
import com.app.MediQuirk.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users addUser(Users user) {
        return usersRepository.save(user);
    }
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }


    public Users updateUser(Long id, Users userDetails) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setIsActive(userDetails.getIsActive());
        user.setRoles(userDetails.getRoles());
        return usersRepository.save(user);
    }

    public void deleteUser(Long id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        usersRepository.delete(user);
    }
}