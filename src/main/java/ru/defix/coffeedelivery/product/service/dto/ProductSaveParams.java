package ru.defix.coffeedelivery.product.service.dto;

import java.math.BigDecimal;

public record ProductSaveParams(int ownerId, String name, BigDecimal price) {
}
