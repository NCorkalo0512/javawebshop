package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
