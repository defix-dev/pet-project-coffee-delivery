package ru.defix.coffeedelivery.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.defix.coffeedelivery.db.entity.ProductSellRequest;
import ru.defix.coffeedelivery.db.entity.ProductUpdateRequest;

import java.util.Set;

@Repository
public interface ProductSellRequestRepository extends JpaRepository<ProductSellRequest, Integer> {
    @Query("SELECT psr FROM ProductSellRequest psr WHERE psr.productRequest.status = 'PENDING'")
    Set<ProductSellRequest> findAllPending();

    Set<ProductSellRequest> findAllByProductRequest_Submitter_Id(int submitterId);
}
