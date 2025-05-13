package ru.defix.coffeedelivery.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.defix.coffeedelivery.db.entity.ProductRequest;

import java.util.Optional;

@Repository
public interface ProductRequestRepository extends JpaRepository<ProductRequest, Integer> {
    boolean existsBySubmitter_IdAndStatus(int submitterId, ProductRequest.Status status);

    @Query("SELECT req FROM ProductRequest req WHERE req.status='PENDING' and req.id=:id")
    Optional<ProductRequest> findActiveRequestById(int id);
}
