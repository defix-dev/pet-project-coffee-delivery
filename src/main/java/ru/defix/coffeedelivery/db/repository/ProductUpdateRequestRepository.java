package ru.defix.coffeedelivery.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.defix.coffeedelivery.db.entity.ProductSellRequest;
import ru.defix.coffeedelivery.db.entity.ProductUpdateRequest;

import java.util.Set;

@Repository
public interface ProductUpdateRequestRepository extends JpaRepository<ProductUpdateRequest, Integer> {
    @Query("SELECT pur FROM ProductUpdateRequest pur WHERE pur.productRequest.status = 'PENDING'")
    Set<ProductUpdateRequest> findAllPending();

    Set<ProductUpdateRequest> findAllByProductRequest_Submitter_Id(int submitterId);
}
