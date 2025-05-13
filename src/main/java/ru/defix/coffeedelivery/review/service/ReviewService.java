package ru.defix.coffeedelivery.review.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.coffeedelivery.db.entity.Review;
import ru.defix.coffeedelivery.db.repository.ReviewRepository;
import ru.defix.coffeedelivery.product.service.ProductService;
import ru.defix.coffeedelivery.review.exception.ReviewAlreadyExistsException;
import ru.defix.coffeedelivery.review.exception.ReviewNotFoundException;
import ru.defix.coffeedelivery.review.service.dto.ReviewCreateParams;
import ru.defix.coffeedelivery.user.service.UserService;

import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService,
                         ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #params.senderId() == principal.id")
    public void createReview(ReviewCreateParams params) {
        if(reviewRepository.existsByProduct_IdAndSender_Id(params.productId(), params.senderId()))
            throw new ReviewAlreadyExistsException();
        Review review = new Review();
        review.setText(params.text());
        review.setSender(userService.getById(params.senderId()));
        review.setProduct(productService.getById(params.productId()));

        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(int reviewId) {
        reviewRepository.delete(getById(reviewId));
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.sender.id == principal.id")
    public Review getById(int id) {
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    public Set<Review> getAllByProductId(int productId) {
        return reviewRepository.findAllByProduct_Id(productId);
    }
}
