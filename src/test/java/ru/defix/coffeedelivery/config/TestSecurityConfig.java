package ru.defix.coffeedelivery.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.defix.coffeedelivery.auth.filter.JwtAccessAuthenticationFilter;
import ru.defix.coffeedelivery.auth.filter.JwtRefreshAuthenticationFilter;
import ru.defix.coffeedelivery.auth.service.UserDetailsServiceImpl;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtRefreshAuthenticationFilter testRefreshAuthenticationFilter() {
        return new JwtRefreshAuthenticationFilter(mock(UserDetailsServiceImpl.class), mock(HandlerExceptionResolver.class)) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                filterChain.doFilter(request, response);
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                return true;
            }
        };
    }

    @Bean
    @Primary
    public JwtAccessAuthenticationFilter testAccessAuthenticationFilter() {
        return new JwtAccessAuthenticationFilter(mock(UserDetailsServiceImpl.class)) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                filterChain.doFilter(request, response);
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                return true;
            }
        };
    }
}
