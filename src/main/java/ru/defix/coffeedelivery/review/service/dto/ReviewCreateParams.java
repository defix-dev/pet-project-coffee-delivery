package ru.defix.coffeedelivery.review.service.dto;

public record ReviewCreateParams(int senderId, int productId, String text) {
}
