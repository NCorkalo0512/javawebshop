package com.example.hralgebrajavawebshop.repository;

import com.example.hralgebrajavawebshop.models.LoginLog;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LoginLogRepositoryInterface extends JpaRepository<LoginLog, Integer> {
    List<LoginLog> findByUser(User user);
    List<LoginLog> findByLoginTimeBetween(Date start, Date end);

    List<LoginLog>findByipAddress(String ipAddress);
}
