package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.models.AuthRequest;
import com.example.hralgebrajavawebshop.models.AuthResponse;
import com.example.hralgebrajavawebshop.models.User;
import com.example.hralgebrajavawebshop.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class AuthController {
 AuthenticationManager authenticationManager;
 JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/login")
 public ResponseEntity<?> login(AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        try {
            List<GrantedAuthority> roleList = new ArrayList<>();
            roleList.add(new SimpleGrantedAuthority("ANONYMOUS"));
            roleList.add(new SimpleGrantedAuthority("ADMIN"));
            roleList.add(new SimpleGrantedAuthority("USER"));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword(), roleList)
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getUsername(), accessToken);


           HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("user", user);

            String ipAddress = httpServletRequest.getRemoteAddr();
            session.setAttribute("ipAddress", ipAddress);
            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

}