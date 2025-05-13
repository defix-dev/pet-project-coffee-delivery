package ru.defix.coffeedelivery.productRequest.api.util;

import ru.defix.coffeedelivery.db.entity.ProductSellRequest;
import ru.defix.coffeedelivery.db.entity.ProductUpdateRequest;
import ru.defix.coffeedelivery.productRequest.api.dto.response.ProductSellRequestData;
import ru.defix.coffeedelivery.productRequest.api.dto.response.ProductUpdateRequestData;

import java.util.List;
import java.util.Set;

public class ProductRequestPreparer {
    public static List<ProductSellRequestData> prepareSellCollectionToResponse(Set<ProductSellRequest> requests) {
        return requests.stream().map(ProductRequestPreparer::prepareSellItemToResponse).toList();
    }

    public static List<ProductUpdateRequestData> prepareUpdateCollectionToResponse(Set<ProductUpdateRequest> requests) {
        return requests.stream().map(ProductRequestPreparer::prepareUpdateItemToResponse).toList();
    }

    public static ProductSellRequestData prepareSellItemToResponse(ProductSellRequest request) {
        return new ProductSellRequestData(
                request.getProductRequest().getId(),
                request.getProductRequest().getSubmitter().getId(),
                request.getName(),
                request.getPrice()
        );
    }

    public static ProductUpdateRequestData prepareUpdateItemToResponse(ProductUpdateRequest request) {
        return new ProductUpdateRequestData(
                request.getProductRequest().getId(),
                request.getProductRequest().getSubmitter().getId(),
                request.getProduct().getId(),
                request.getName(),
                request.getPrice()
        );
    }
}
