package ru.defix.coffeedelivery.product.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.coffeedelivery.db.entity.Product;
import ru.defix.coffeedelivery.db.repository.ProductRepository;
import ru.defix.coffeedelivery.product.api.dto.request.ProductFilterData;
import ru.defix.coffeedelivery.product.exception.ProductAlreadyExistsException;
import ru.defix.coffeedelivery.product.exception.ProductNotFoundException;
import ru.defix.coffeedelivery.product.service.dto.ProductSaveParams;
import ru.defix.coffeedelivery.product.service.dto.ProductUpdateParams;
import ru.defix.coffeedelivery.product.service.util.ProductSpecifications;
import ru.defix.coffeedelivery.productRequest.service.event.OnSellProductRequestEvent;
import ru.defix.coffeedelivery.productRequest.service.event.OnUpdateProductRequestEvent;
import ru.defix.coffeedelivery.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @EventListener
    public void onSellProductRequest(OnSellProductRequestEvent event) {
        if(productRepository
                .existsByOwner_IdAndNameAndPrice(event.submitterId(), event.name(), event.price())) throw new ProductAlreadyExistsException();
        saveProduct(new ProductSaveParams(
            event.submitterId(), event.name(), event.price()
        ));
    }

    @EventListener
    public void onUpdateProductRequest(OnUpdateProductRequestEvent event) {
        updateProduct(new ProductUpdateParams(
                event.productId(),
                event.name(),
                event.price()
        ));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or #params.ownerId() == principal.id")
    public void saveProduct(ProductSaveParams params) {
        Product product = new Product();
        product.setName(params.name());
        product.setPrice(params.price());
        product.setUpdatedAt(Timestamp.from(Instant.now()));
        product.setOwner(userService.getById(params.ownerId()));

        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(ProductUpdateParams params) {
        Product updatedProduct = getById(params.productId());
        if(params.name() != null) updatedProduct.setName(params.name());
        if(params.price() != null) updatedProduct.setPrice(params.price());
    }

    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or returnObject.owner.id == principal.id")
    public Product getById(int id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Page<Product> findProductsByFilterFromPage(ProductFilterData filterData, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);

        if(filterData.minPrice() != null) spec = spec.and(ProductSpecifications.priceGreaterThanOrEqual(filterData.minPrice()));
        if(filterData.maxPrice() != null) spec = spec.and(ProductSpecifications.priceLessThanOrEqual(filterData.maxPrice()));
        if(filterData.price() != null) spec = spec.and(ProductSpecifications.priceEquals(filterData.price()));
        if(filterData.name() != null) spec = spec.and(ProductSpecifications.nameContains(filterData.name()));
        if(filterData.ownerId() != null) spec = spec.and(ProductSpecifications.ownerIdEquals(filterData.ownerId()));
        if(filterData.ownerName() != null) spec = spec.and(ProductSpecifications.ownerNameContains(filterData.ownerName()));

        return productRepository.findAll(spec, pageable);
    }
}
