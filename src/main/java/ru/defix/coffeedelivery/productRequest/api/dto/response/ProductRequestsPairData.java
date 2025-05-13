package ru.defix.coffeedelivery.productRequest.api.dto.response;

import java.util.List;

public record ProductRequestsPairData(List<ProductSellRequestData> sellRequestData,
                                      List<ProductUpdateRequestData> updateRequestData) {
}
