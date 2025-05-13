package ru.defix.coffeedelivery.product.api.util;

import org.springframework.data.domain.Page;
import ru.defix.coffeedelivery.db.entity.Product;
import ru.defix.coffeedelivery.product.api.dto.response.ProductData;

public class ProductPreparer {
    public static Page<ProductData> prepareProductToProductDataCollection(Page<Product> products) {
        return products.map(product -> new ProductData(
                product.getName(),
                product.getPrice(),
                product.getOwner().getId(),
                product.getUpdatedAt()
        ));
    }
}
