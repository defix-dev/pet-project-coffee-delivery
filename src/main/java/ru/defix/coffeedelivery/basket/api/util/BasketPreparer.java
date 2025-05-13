package ru.defix.coffeedelivery.basket.api.util;

import ru.defix.coffeedelivery.basket.api.dto.response.BasketPersonalData;
import ru.defix.coffeedelivery.basket.api.dto.response.ProductPersonalData;
import ru.defix.coffeedelivery.db.entity.Basket;

import java.util.List;
import java.util.stream.Collectors;

public class BasketPreparer {
    public static List<BasketPersonalData> prepareBasketsToPersonalData(List<Basket> baskets) {
        return baskets.stream().map(basket -> new BasketPersonalData(
                basket.getQuantity(),
                new ProductPersonalData(
                        basket.getProduct().getName(),
                        basket.getProduct().getPrice(),
                        basket.getProduct().getId(),
                        basket.getProduct().getOwner().getId()
                )
        )).toList();
    }
}
