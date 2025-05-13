package ru.defix.coffeedelivery.productRequest.service.event;

import java.math.BigDecimal;

public record OnSellProductRequestEvent(int submitterId, String name, BigDecimal price) {
}
