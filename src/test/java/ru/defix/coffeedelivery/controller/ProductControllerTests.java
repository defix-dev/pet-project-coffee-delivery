package ru.defix.coffeedelivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.defix.coffeedelivery.auth.config.SecurityConfig;
import ru.defix.coffeedelivery.basket.api.controller.BasketControllerV1;
import ru.defix.coffeedelivery.config.TestSecurityConfig;
import ru.defix.coffeedelivery.db.entity.Product;
import ru.defix.coffeedelivery.db.entity.User;
import ru.defix.coffeedelivery.product.api.controller.ProductControllerV1;
import ru.defix.coffeedelivery.product.api.dto.request.ProductFilterData;
import ru.defix.coffeedelivery.product.api.dto.response.ProductData;
import ru.defix.coffeedelivery.product.api.util.ProductPreparer;
import ru.defix.coffeedelivery.product.service.ProductService;
import ru.defix.coffeedelivery.util.TestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControllerV1.class)
@Import({TestSecurityConfig.class, SecurityConfig.class})
@ControllerTests
@WithMockUser(roles = "USER")
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Nested
    public class SuccessTests {
        @Test
        public void findProducts_WithValidFilter_ReturnsOkAndCorrectJson() throws Exception {
            Product productData = new Product();
            productData.setName("Latte");
            productData.setPrice(BigDecimal.valueOf(149));
            productData.setUpdatedAt(Timestamp.from(Instant.now()));
            User u = new User();
            u.setId(1);
            productData.setOwner(u);

            Page<Product> page = new PageImpl<>(List.of(productData), PageRequest.of(0, 10), 1);

            when(productService.findProductsByFilterFromPage(any(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/api/v1/products")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(TestUtils.asJsonString(ProductPreparer.prepareProductToProductDataCollection(
                            page
                    ))));
        }
    }

    @Nested
    public class FailureTests {
        @Test
        public void findProducts_InvalidPriceParam_ReturnsBadRequest() throws Exception {
            mockMvc.perform(get("/api/v1/products")
                            .param("minPrice", "NaN")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
