package com.example.hralgebrajavawebshop.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword=passwordEncoder.encode("user");
        System.out.println(encodedPassword);
    }
}
