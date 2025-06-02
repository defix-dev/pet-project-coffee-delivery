package ru.defix.coffeedelivery.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.defix.coffeedelivery.auth.config.JwtConfig;
import ru.defix.coffeedelivery.auth.config.SecurityConfig;
import ru.defix.coffeedelivery.auth.service.UserDetailsServiceImpl;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.basket.api.controller.BasketControllerV1;
import ru.defix.coffeedelivery.basket.api.dto.response.BasketPersonalData;
import ru.defix.coffeedelivery.basket.api.dto.response.ProductPersonalData;
import ru.defix.coffeedelivery.basket.service.BasketService;
import ru.defix.coffeedelivery.basket.service.dto.BasketCreaseParams;
import ru.defix.coffeedelivery.config.TestSecurityConfig;
import ru.defix.coffeedelivery.db.entity.Basket;
import ru.defix.coffeedelivery.db.entity.Product;
import ru.defix.coffeedelivery.db.entity.User;
import ru.defix.coffeedelivery.util.TestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasketControllerV1.class)
@Import({TestSecurityConfig.class, SecurityConfig.class})
@ControllerTests
public class BasketControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BasketService basketService;

    @Nested
    public class SuccessTests {

        @Test
        public void getPersonalBasketsTest() throws Exception {
            TestUtils.changeDefaultUserDetails();

            Basket data = new Basket();
            data.setQuantity(100);
            Product p = new Product();
            p.setId(1);
            p.setName("name");
            p.setPrice(BigDecimal.valueOf(100));
            User u = new User();
            u.setId(1);
            p.setOwner(u);
            data.setProduct(p);

            when(basketService.getAllByUserId(1)).thenReturn(Collections.singletonList(data));

            mockMvc.perform(get("/api/v1/baskets/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        public void increasePersonalBasketTest() throws Exception {
            TestUtils.changeDefaultUserDetails();

            doNothing().when(basketService).increaseBasket(any(BasketCreaseParams.class));

            mockMvc.perform(post("/api/v1/baskets/me")
                            .param("productId", "1")
                            .param("quantity", "2"))
                    .andExpect(status().isNoContent());

            verify(basketService).increaseBasket(any(BasketCreaseParams.class));
        }

        @Test
        public void decreasePersonalBasketTest() throws Exception {
            TestUtils.changeDefaultUserDetails();

            doNothing().when(basketService).decreaseBasket(any(BasketCreaseParams.class));

            mockMvc.perform(delete("/api/v1/baskets/me")
                            .param("productId", "1")
                            .param("quantity", "1"))
                    .andExpect(status().isNoContent());

            verify(basketService).decreaseBasket(any(BasketCreaseParams.class));
        }
    }

    @Nested
    public class FailureTests {

        @Test
        @WithMockUser(roles = "USER")
        public void increasePersonalBasketTest_WithoutProductId() throws Exception {
            TestUtils.changeDefaultUserDetails();

            mockMvc.perform(post("/api/v1/baskets/me"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void decreasePersonalBasketTest_WithoutProductId() throws Exception {
            TestUtils.changeDefaultUserDetails();

            mockMvc.perform(delete("/api/v1/baskets/me"))
                    .andExpect(status().isBadRequest());
        }
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
