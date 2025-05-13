package ru.defix.coffeedelivery.product.api.dto.response;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ProductData(
        String name,
        BigDecimal price,
        Integer ownerId,
        Timestamp updatedAt
) { }
