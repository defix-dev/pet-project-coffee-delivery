package ru.defix.coffeedelivery.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.review.api.dto.request.ReviewCreateData;
import ru.defix.coffeedelivery.review.api.dto.response.ReviewData;
import ru.defix.coffeedelivery.review.api.dto.response.SenderData;
import ru.defix.coffeedelivery.review.api.facade.ReviewControllerFacade;
import ru.defix.coffeedelivery.util.TestUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ControllerTests
public class ReviewControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewControllerFacade reviewFacade;

    @Nested
    public class SuccessTests {
        @Test
        @WithMockUser(roles = "USER")
        public void getAllReviewsByProductIdTest() throws Exception {
            List<ReviewData> dataToTest = Collections.singletonList(new ReviewData(
                    1, new SenderData(1, "username"), "text", Timestamp.from(Instant.now())
            ));
            when(reviewFacade.getAllReviewsByProductId(1)).thenReturn(dataToTest);

            mockMvc.perform(get("/api/v1/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("productId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(TestUtils.asJsonString(dataToTest)));
        }

        @Test
        public void createReviewTest() throws Exception {
            TestUtils.changeDefaultUserDetails();
            ReviewCreateData createData = new ReviewCreateData(
                    1,
                    "text"
            );
            doNothing().when(reviewFacade).createReview(any(), anyInt());

            mockMvc.perform(post("/api/v1/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.asJsonString(createData)))
                    .andExpect(status().isNoContent());

            verify(reviewFacade).createReview(refEq(createData), eq(1));
        }

        @Test
        @WithMockUser(roles = "USER")
        public void deleteReviewTest() throws Exception {
            doNothing().when(reviewFacade).deleteReview(anyInt());

            mockMvc.perform(delete("/api/v1/reviews")
                    .param("reviewId", "1"))
                    .andExpect(status().isNoContent());

            verify(reviewFacade).deleteReview(1);
        }
    }

    @Nested
    public class FailureTests {
        @Test
        @WithMockUser(roles = "USER")
        public void getAllReviewsByProductIdTest_WithoutParam() throws Exception {
            List<ReviewData> dataToTest = Collections.singletonList(new ReviewData(
                    1, new SenderData(1, "username"), "text", Timestamp.from(Instant.now())
            ));
            when(reviewFacade.getAllReviewsByProductId(1)).thenReturn(dataToTest);

            mockMvc.perform(get("/api/v1/reviews")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void createReviewTest_WithoutContent() throws Exception {
            TestUtils.changeDefaultUserDetails();
            doNothing().when(reviewFacade).createReview(any(), anyInt());

            mockMvc.perform(post("/api/v1/reviews")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "USER")
        public void deleteReviewTest_WithoutParam() throws Exception {
            doNothing().when(reviewFacade).deleteReview(anyInt());

            mockMvc.perform(delete("/api/v1/reviews"))
                    .andExpect(status().isBadRequest());
        }
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
