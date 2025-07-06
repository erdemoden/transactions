package com.woh.transactions.filter;

import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.services.JWTService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter implements Filter {

    private final JWTService jwtService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html") || path.startsWith("/tmp/") || path.startsWith("/api/users/login") || path.startsWith("/api/users/register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        else{
            log.info(path + " is not a public endpoint, checking JWT token");
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.isJwtValid(token).containsKey(true)) {
                    log.info("JWT token is valid");
                    servletResponse.getWriter().write(ErrorSuccess.builder()
                            .message("JWT token is invalid or expired")
                            .success(false)
                            .data(null)
                            .build().toString());
                }
            } catch (Exception e) {
                log.error("Error validating JWT token: {}", e.getMessage());
                servletResponse.getWriter().write(ErrorSuccess.builder()
                        .message("Error validating JWT token")
                        .success(false)
                        .data(null)
                        .build().toString());
                return;
            }
        } else {
            log.info("No JWT token provided in the request");
            servletResponse.getWriter().write(ErrorSuccess.builder()
                    .message("No JWT token provided")
                    .success(false)
                    .data(null)
                    .build().toString());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
