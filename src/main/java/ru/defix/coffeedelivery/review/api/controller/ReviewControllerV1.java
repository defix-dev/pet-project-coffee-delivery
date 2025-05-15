package ru.defix.coffeedelivery.review.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.review.api.dto.request.ReviewCreateData;
import ru.defix.coffeedelivery.review.api.dto.response.ReviewData;
import ru.defix.coffeedelivery.review.api.facade.ReviewControllerFacade;
import ru.defix.coffeedelivery.review.api.util.ReviewPreparer;
import ru.defix.coffeedelivery.review.service.ReviewService;
import ru.defix.coffeedelivery.review.service.dto.ReviewCreateParams;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewControllerV1 {
    private final ReviewControllerFacade facade;

    @Autowired
    public ReviewControllerV1(ReviewControllerFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public ResponseEntity<List<ReviewData>> getAllReviewsByProductId(@RequestParam int productId) {
        return ResponseEntity.ok(facade.getAllReviewsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewCreateData data,
                                          @AuthenticationPrincipal SimpleUserDetails userDetails) {
        facade.createReview(data, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReview(@RequestParam int reviewId) {
        facade.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
