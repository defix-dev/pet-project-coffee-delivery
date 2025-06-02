package ru.defix.coffeedelivery.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.defix.coffeedelivery.auth.api.controller.AuthControllerV1;
import ru.defix.coffeedelivery.auth.api.dto.request.LoginRequest;
import ru.defix.coffeedelivery.auth.api.dto.request.RefreshTokenRequest;
import ru.defix.coffeedelivery.auth.api.dto.request.RegisterRequest;
import ru.defix.coffeedelivery.auth.api.dto.response.JwtPairResponse;
import ru.defix.coffeedelivery.auth.filter.JwtAccessAuthenticationFilter;
import ru.defix.coffeedelivery.auth.filter.JwtRefreshAuthenticationFilter;
import ru.defix.coffeedelivery.auth.service.AuthService;
import ru.defix.coffeedelivery.auth.service.jwt.JwtConstants;
import ru.defix.coffeedelivery.auth.service.jwt.JwtUtils;
import ru.defix.coffeedelivery.config.TestSecurityConfig;
import ru.defix.coffeedelivery.user.service.UserService;
import ru.defix.coffeedelivery.user.service.dto.UserSaveParams;
import ru.defix.coffeedelivery.util.TestUtils;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthControllerV1.class)
@AutoConfigureMockMvc(addFilters = false)
@ControllerTests
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @Nested
    public class SuccessTests {
        @Test
        public void registerTest() throws Exception {
            UserSaveParams saveParams = new UserSaveParams(
                    "username",
                    "password",
                    "email@gmail.com"
            );
            doNothing().when(userService).save(any());

            mockMvc.perform(post("/api/v1/auth/register")
                    .content(TestUtils.asJsonString(saveParams))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(userService).save(refEq(saveParams));
        }

        @Test
        public void loginTest() throws Exception {
            LoginRequest loginRequest = new LoginRequest(
                    "username", "password"
            );
            JwtPairResponse jwtPair = new JwtPairResponse("accessToken", "refreshToken");
            when(authService.login(any())).thenReturn(jwtPair);

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(cookie().value(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, jwtPair.accessToken()))
                    .andExpect(cookie().value(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, jwtPair.refreshToken()))
                    .andExpect(jsonPath(String.format("$.%s", JwtConstants.ACCESS_TOKEN_COOKIE_NAME)).value("accessToken"))
                    .andExpect(jsonPath(String.format("$.%s", JwtConstants.REFRESH_TOKEN_COOKIE_NAME)).value("refreshToken"));
        }

        @Test
        public void logoutTest() throws Exception {
            mockMvc.perform(post("/api/v1/auth/logout"))
                    .andExpect(status().isNoContent())
                    .andExpect(cookie().maxAge(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, 0))
                    .andExpect(cookie().maxAge(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, 0));
        }

        @Test
        public void refreshAccessTokenTest() throws Exception {
            try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
                TestUtils.changeDefaultUserDetails();
                RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");
                JwtPairResponse jwtPair = new JwtPairResponse("accessToken", "refreshToken");
                jwtUtilsMock.when(() -> JwtUtils.refreshAccessToken(anyString(), anyList())).thenReturn(
                        "accessToken"
                );

                mockMvc.perform(post("/api/v1/auth/jwt/refresh-json")
                                .content(TestUtils.asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(TestUtils.asJsonString(jwtPair)));
            }
        }
    }
}
