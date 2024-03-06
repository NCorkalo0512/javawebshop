package com.example.hralgebrajavawebshop.repository;


import com.example.hralgebrajavawebshop.dto.LoginLogDTO;
import com.example.hralgebrajavawebshop.models.LoginLog;
import com.example.hralgebrajavawebshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class LoginLogRepository {
    private final LoginLogRepositoryInterface loginLogRepositoryInterface;
    private final UserRepositoryInterface userRepositoryInterface;
    @Autowired
    public LoginLogRepository(LoginLogRepositoryInterface loginLogRepositoryInterface, UserRepositoryInterface userRepositoryInterface) {
        this.loginLogRepositoryInterface = loginLogRepositoryInterface;
        this.userRepositoryInterface = userRepositoryInterface;
    }

    public LoginLog addLoginLog(LoginLogDTO loginLogDTO) {
        User user = userRepositoryInterface.findById(loginLogDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginLog loginLog = new LoginLog();
        loginLog.setLoginTime(loginLogDTO.getLoginTime());
        loginLog.setIpAddress(loginLogDTO.getIpAddress());
        loginLog.setUser(user);

        return loginLogRepositoryInterface.save(loginLog);
    }

    public LoginLog updateLoginLog(int loginLogId, LoginLogDTO loginLogDTO) {
        LoginLog existingLoginLog = loginLogRepositoryInterface.findById(loginLogId)
                .orElseThrow(() -> new RuntimeException("Login log not found"));

        User user = userRepositoryInterface.findById(loginLogDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingLoginLog.setLoginTime(loginLogDTO.getLoginTime());
        existingLoginLog.setIpAddress(loginLogDTO.getIpAddress());
        existingLoginLog.setUser(user);

        return loginLogRepositoryInterface.save(existingLoginLog);
    }
    public void deleteLoginLogById(Integer id){
        loginLogRepositoryInterface.deleteById(id);
    }

    public List<LoginLog> findAll(){
        return loginLogRepositoryInterface.findAll();
    }

    public Optional<LoginLog>findById(Integer id){
        return loginLogRepositoryInterface.findById(id);
    }
    public List<LoginLog> findByUser(User user){
        return loginLogRepositoryInterface.findByUser(user);
    }
    public List<LoginLog> findByLoginTimeBetween(Date start, Date end){
        return loginLogRepositoryInterface.findByLoginTimeBetween(start,end);
    }

    public List<LoginLog> findByIPAddress(String ipAddress){
        return loginLogRepositoryInterface.findByipAddress(ipAddress);
    }
}
