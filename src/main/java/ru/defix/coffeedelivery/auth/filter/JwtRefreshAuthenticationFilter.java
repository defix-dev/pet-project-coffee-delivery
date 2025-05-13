package ru.defix.coffeedelivery.auth.filter;

import org.springframework.security.core.userdetails.UserDetails;
import ru.defix.coffeedelivery.auth.service.UserDetailsServiceImpl;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.auth.service.jwt.JwtConstants;
import ru.defix.coffeedelivery.auth.service.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshAuthenticationFilter extends OncePerRequestFilter {
    private final static RequestMatcher accessMatcher = new AntPathRequestMatcher("/api/v1/auth/jwt/**");
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final UserDetailsServiceImpl userDetailsService;

    public JwtRefreshAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletRequest requestWrapper = new CachedBodyHttpServletRequest(request);
        String refreshToken = objectMapper
                .readTree(requestWrapper.getInputStream())
                .get(JwtConstants.REFRESH_TOKEN_COOKIE_NAME).asText();
        if(!JwtUtils.validateToken(refreshToken)) throw new InsufficientAuthenticationException("Invalid Signature");
        String username = JwtUtils.convertStringToRefreshTokenDetails(refreshToken).subject();
        SimpleUserDetails userDetails = (SimpleUserDetails) userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(requestWrapper, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !accessMatcher.matches(request);
    }
}
