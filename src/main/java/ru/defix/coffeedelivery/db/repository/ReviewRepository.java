package ru.defix.coffeedelivery.db.repository;

import ru.defix.coffeedelivery.db.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByProduct_IdAndSender_Id(int productId, int senderId);
    Set<Review> findAllByProduct_Id(int productId);
}
