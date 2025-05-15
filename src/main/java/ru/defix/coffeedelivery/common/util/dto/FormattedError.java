package ru.defix.coffeedelivery.common.util.dto;

import java.time.LocalDateTime;

public record FormattedError(String error, int status, String message, String path, LocalDateTime timestamp) {
}
