package com.example.hralgebrajavawebshop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LoginLogs")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLoginLog;

    @Column(name = "LoginTime")
    private Date loginTime;

    @Column(name = "IPAddress")
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "UsersID", referencedColumnName = "idUsers")
    private User user;
}
