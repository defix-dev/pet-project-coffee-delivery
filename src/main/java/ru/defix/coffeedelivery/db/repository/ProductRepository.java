package ru.defix.coffeedelivery.db.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.defix.coffeedelivery.db.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>,
        JpaSpecificationExecutor<Product> {
    boolean existsByOwner_IdAndNameAndPrice(int ownerId, String name, BigDecimal price);
}
