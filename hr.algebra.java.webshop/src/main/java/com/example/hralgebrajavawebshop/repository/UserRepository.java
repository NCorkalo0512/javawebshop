package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final UserRepositoryInterface userRepositoryInterface;

    @Autowired
    public UserRepository(UserRepositoryInterface userRepositoryInterface) {
        this.userRepositoryInterface = userRepositoryInterface;
    }

    @Transactional
    public User findUserByUsername(String username) {
        return userRepositoryInterface.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepositoryInterface.findAll();
    }

    @Transactional
    public Optional<User> findUserById(Integer id) {
        return userRepositoryInterface.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepositoryInterface.save(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepositoryInterface.deleteById(id);
    }
}
