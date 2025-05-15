package ru.defix.coffeedelivery.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.defix.coffeedelivery.auth.filter.JwtRefreshAuthenticationFilter;

import java.io.IOException;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    @Primary
    public JwtRefreshAuthenticationFilter jwtRefreshAuthenticationFilter() {
        return new JwtRefreshAuthenticationFilter(null, null) {
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                filterChain.doFilter(request, response);
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                return true;
            }
        };
    }
}
