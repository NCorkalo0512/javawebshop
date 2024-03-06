package com.example.hralgebrajavawebshop.filter;

import com.example.hralgebrajavawebshop.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;


@Component
public class AuthFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private final JwtTokenUtil jwtTokenUtil;

    public AuthFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if(authentication.isAuthenticated()){
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        else{
        String jwtToken = getJwtFromRequest(httpRequest);
        if(!isLoginOrErrorPageRequest(httpRequest))
        {
            if (isUserLoggedIn(httpRequest) || isValidToken(jwtToken, httpRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                LOGGER.info("Unauthorized access request");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must log in to access this resource");
            }
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
        }
    }
    private boolean isUserLoggedIn(HttpServletRequest httpRequest) {
        Object user = httpRequest.getSession().getAttribute("user");
        return user != null;
    }
    private boolean isLoginOrErrorPageRequest(HttpServletRequest httpRequest) {
        var loginUri = httpRequest.getRequestURI();
        var isLoginOrErrorPage = loginUri.contains("login") || loginUri.contains("error")|| loginUri.contains("list") || loginUri.contains("market") || loginUri.contains("categories") || loginUri.contains("api");
        return isLoginOrErrorPage;
    }
    private boolean isValidToken(String jwtToken, HttpServletRequest httpRequest) {
        if(isLoginOrErrorPageRequest(httpRequest)){
            return true;
        }
        try {
            if(jwtToken == null){
                var endpoint = httpRequest.getRequestURI();
                throw new Exception(endpoint);
            }
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtTokenUtil.SECRET_KEY).parseClaimsJws(jwtToken);

            return true;
        } catch (Exception e) {
            LOGGER.info("Invalid JWT token");
            LOGGER.info(e.getMessage());
            return false;
        }
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    @Override
    public void destroy() {
        LOGGER.info("LoginFilter destroyed");
    }

}
