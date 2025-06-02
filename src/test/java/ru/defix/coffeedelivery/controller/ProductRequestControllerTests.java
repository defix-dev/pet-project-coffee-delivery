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
import ru.defix.coffeedelivery.auth.config.SecurityConfig;
import ru.defix.coffeedelivery.basket.api.controller.BasketControllerV1;
import ru.defix.coffeedelivery.config.TestSecurityConfig;
import ru.defix.coffeedelivery.productRequest.api.controller.ProductRequestControllerV1;
import ru.defix.coffeedelivery.productRequest.api.dto.request.ProductRequestCreateData;
import ru.defix.coffeedelivery.productRequest.api.dto.request.ProductRequestUpdateData;
import ru.defix.coffeedelivery.productRequest.api.dto.response.ProductRequestsPairData;
import ru.defix.coffeedelivery.productRequest.api.util.ProductRequestPreparer;
import ru.defix.coffeedelivery.productRequest.service.ProductRequestService;
import ru.defix.coffeedelivery.productRequest.service.ProductSellRequestService;
import ru.defix.coffeedelivery.productRequest.service.ProductUpdateRequestService;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductUpdateRequestCreateParams;
import ru.defix.coffeedelivery.util.TestUtils;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRequestControllerV1.class)
@Import({TestSecurityConfig.class, SecurityConfig.class})
@ControllerTests
public class ProductRequestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductSellRequestService sellService;

    @MockitoBean
    private ProductUpdateRequestService updateService;

    @MockitoBean
    private ProductRequestService requestService;

    @Nested
    public class SuccessTests {

        @Test
        @WithMockUser(roles = "MODERATOR")
        public void getAllRequestsTest() throws Exception {
            when(sellService.getAllPendingRequests()).thenReturn(Collections.emptySet());
            when(updateService.getAllPendingRequests()).thenReturn(Collections.emptySet());

            mockMvc.perform(get("/api/v1/product-requests"))
                    .andExpect(status().isOk());
        }

        @Test
        public void getMyRequestsTest() throws Exception {
            TestUtils.changeDefaultUserDetails();

            when(sellService.getAllRequestsBySubmitterId(1)).thenReturn(Collections.emptySet());
            when(updateService.getAllRequestsBySubmitterId(1)).thenReturn(Collections.emptySet());

            mockMvc.perform(get("/api/v1/product-requests/me"))
                    .andExpect(status().isOk());
        }

        @Test
        public void createPersonalSellRequestTest() throws Exception {
            TestUtils.changeDefaultUserDetails();
            ProductRequestCreateData data = new ProductRequestCreateData("Espresso", BigDecimal.valueOf(100));

            mockMvc.perform(post("/api/v1/product-requests/me/sell")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(data)))
                    .andExpect(status().isNoContent());

            verify(sellService).createRequest(any());
        }

        @Test
        public void createPersonalUpdateRequestTest() throws Exception {
            TestUtils.changeDefaultUserDetails();
            ProductRequestUpdateData data = new ProductRequestUpdateData(1, "Espresso New", BigDecimal.valueOf(100));

            doNothing().when(updateService).createRequest(any());

            mockMvc.perform(post("/api/v1/product-requests/me/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(data)))
                    .andExpect(status().isNoContent());

            verify(updateService).createRequest(refEq(new ProductUpdateRequestCreateParams(
                    data.productId(),
                    1,
                    data.name(),
                    data.price()
            )));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void approveRequestTest() throws Exception {
            mockMvc.perform(patch("/api/v1/product-requests/1/approve"))
                    .andExpect(status().isNoContent());

            verify(requestService).approveRequest(1);
        }

        @Test
        @WithMockUser(roles = "MODERATOR")
        public void rejectRequestTest() throws Exception {
            mockMvc.perform(patch("/api/v1/product-requests/1/reject"))
                    .andExpect(status().isNoContent());

            verify(requestService).rejectRequest(1);
        }
    }

    @Nested
    public class FailureTests {

        @Test
        public void createSellRequest_WithoutContent() throws Exception {
            TestUtils.changeDefaultUserDetails();

            mockMvc.perform(post("/api/v1/product-requests/me/sell")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void createUpdateRequest_WithoutContent() throws Exception {
            TestUtils.changeDefaultUserDetails();

            mockMvc.perform(post("/api/v1/product-requests/me/update")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void approveRequest_WithoutAuth() throws Exception {
            mockMvc.perform(patch("/api/v1/product-requests/1/approve"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void rejectRequest_WithoutAuth() throws Exception {
            mockMvc.perform(patch("/api/v1/product-requests/1/reject"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void getAllRequests_WithoutRole() throws Exception {
            mockMvc.perform(get("/api/v1/product-requests"))
                    .andExpect(status().isForbidden());
        }
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
