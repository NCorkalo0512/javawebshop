package com.example.hralgebrajavawebshop.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@WebListener
public class CustomSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String ipAddress = (String) session.getAttribute("ipAddress");
        if (ipAddress != null) {
            System.out.println("User's IP address: " + ipAddress);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se){
        System.out.println("Session destroyed!");

    }
}
