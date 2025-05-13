package ru.defix.coffeedelivery.review.api.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.common.util.CodingUtility;
import ru.defix.coffeedelivery.review.api.dto.request.ReviewCreateData;
import ru.defix.coffeedelivery.review.api.dto.response.ReviewData;
import ru.defix.coffeedelivery.review.api.util.ReviewPreparer;
import ru.defix.coffeedelivery.review.service.ReviewService;
import ru.defix.coffeedelivery.review.service.dto.ReviewCreateParams;

import java.util.List;

@Component
public class ReviewControllerFacade {
    private final ReviewService reviewService;

    @Autowired
    public ReviewControllerFacade(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public List<ReviewData> getAllReviewsByProductId(int productId) {
        return ReviewPreparer.prepareReviewCollectionToResponse(
                reviewService.getAllByProductId(productId));
    }

    public void createReview(ReviewCreateData data, int senderId) {
        reviewService.createReview(new ReviewCreateParams(
                senderId,
                data.productId(),
                CodingUtility.encode(data.text())
        ));
    }

    public void deleteReview(int reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
