package ru.defix.coffeedelivery.auth.api.dto.response;

public record JwtPairResponse(String accessToken, String refreshToken) {
}
