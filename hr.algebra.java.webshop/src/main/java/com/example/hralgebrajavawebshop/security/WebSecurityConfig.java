package com.example.hralgebrajavawebshop.security;


import com.example.hralgebrajavawebshop.filter.AuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    private CustomUserDetailsService userDetailsService;
    private final AuthFilter authFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/api/listProducts")
                        .permitAll()
                )
                .authorizeHttpRequests((requests) ->
                        /*requests.requestMatchers("/market.png").permitAll()
                                .requestMatchers("/auth/login", "/error","/api/categories/listCategories", "/api/listProducts").permitAll()
                        .requestMatchers("/login", "/static/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/orders/orderHistory").hasRole("USER")*/
                        requests.anyRequest().permitAll()
                )
                .csrf(CsrfConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                );

        http.addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

@Autowired
    public void  configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
     auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());
}

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {

                response.setStatus(HttpServletResponse.SC_OK);

                getRedirectStrategy().sendRedirect(request, response, "/api/products");
            }
        };
    }
}