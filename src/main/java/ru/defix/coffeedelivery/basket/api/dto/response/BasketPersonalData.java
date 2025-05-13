package ru.defix.coffeedelivery.basket.api.dto.response;

import java.math.BigDecimal;

public record BasketPersonalData(
        int quantity,
        ProductPersonalData productData
) { }
