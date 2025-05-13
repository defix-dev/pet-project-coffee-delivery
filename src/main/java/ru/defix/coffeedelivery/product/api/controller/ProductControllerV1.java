package ru.defix.coffeedelivery.product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.defix.coffeedelivery.product.api.dto.request.ProductFilterData;
import ru.defix.coffeedelivery.product.api.dto.response.ProductData;
import ru.defix.coffeedelivery.product.api.util.ProductPreparer;
import ru.defix.coffeedelivery.product.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {
    private final ProductService productService;

    @Autowired
    public ProductControllerV1(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductData>> findProducts(@ModelAttribute ProductFilterData filterData, Pageable pageable) {
        return ResponseEntity.ok(ProductPreparer.prepareProductToProductDataCollection(
                productService.findProductsByFilterFromPage(filterData, pageable)
        ));
    }
}
