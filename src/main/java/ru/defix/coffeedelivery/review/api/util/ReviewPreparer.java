package ru.defix.coffeedelivery.review.api.util;

import ru.defix.coffeedelivery.common.util.CodingUtility;
import ru.defix.coffeedelivery.db.entity.Review;
import ru.defix.coffeedelivery.review.api.dto.response.ReviewData;
import ru.defix.coffeedelivery.review.api.dto.response.SenderData;

import java.util.List;
import java.util.Set;

public class ReviewPreparer {
    public static List<ReviewData> prepareReviewCollectionToResponse(Set<Review> reviews) {
        return reviews.stream().map(ReviewPreparer::prepareReviewItemToResponse).toList();
    }

    public static ReviewData prepareReviewItemToResponse(Review review) {
        return new ReviewData(review.getId(),
                new SenderData(
                        review.getSender().getId(),
                        review.getSender().getUsername()
                ),
                CodingUtility.decode(review.getText()), review.getCreatedAt()
        );
    }
}
