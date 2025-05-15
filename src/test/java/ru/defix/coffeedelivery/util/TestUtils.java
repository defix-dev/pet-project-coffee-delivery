package ru.defix.coffeedelivery.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;

import java.util.Collections;

public class TestUtils {
    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeCustomUserDetails(SimpleUserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void changeDefaultUserDetails() {
        TestUtils.changeCustomUserDetails(new SimpleUserDetails(
                1,
                "username",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        ));
    }
}
