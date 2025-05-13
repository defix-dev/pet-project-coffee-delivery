package ru.defix.coffeedelivery.product.service.util;

import org.springframework.data.jpa.domain.Specification;
import ru.defix.coffeedelivery.db.entity.Product;

import java.math.BigDecimal;

public class ProductSpecifications {
    public static Specification<Product> nameContains(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), String.format("%%s%", name));
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> priceEquals(BigDecimal price) {
        return (root, query, builder) -> builder.equal(root.get("price"), price);
    }

    public static Specification<Product> ownerIdEquals(Integer ownerId) {
        return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Product> ownerNameContains(String ownerName) {
        return (root, query, builder) -> builder.like(root.get("owner").get("name"), String.format("%%s%", ownerName));
    }
}
