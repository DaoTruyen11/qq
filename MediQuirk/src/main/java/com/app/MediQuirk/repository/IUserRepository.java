package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByUsernameOrEmailOrPhone(String username, String email, String phone);
}