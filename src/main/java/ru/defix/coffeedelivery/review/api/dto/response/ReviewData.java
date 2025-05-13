package ru.defix.coffeedelivery.review.api.dto.response;

import java.sql.Timestamp;

public record ReviewData(int id, SenderData senderData, String text, Timestamp createdAt) {
}
