package ru.defix.coffeedelivery.db.repository;

import ru.defix.coffeedelivery.db.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {
    Optional<Basket> findByUser_IdAndProduct_Id(int userId, int productId);
    List<Basket> findAllByUser_Id(int userId);
}
