package ru.defix.coffeedelivery.productRequest.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.productRequest.api.dto.request.ProductRequestCreateData;
import ru.defix.coffeedelivery.productRequest.api.dto.request.ProductRequestUpdateData;
import ru.defix.coffeedelivery.productRequest.api.dto.response.ProductRequestsPairData;
import ru.defix.coffeedelivery.productRequest.api.util.ProductRequestPreparer;
import ru.defix.coffeedelivery.productRequest.service.ProductRequestService;
import ru.defix.coffeedelivery.productRequest.service.ProductSellRequestService;
import ru.defix.coffeedelivery.productRequest.service.ProductUpdateRequestService;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductSellRequestCreateParams;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductUpdateRequestCreateParams;

@RestController
@RequestMapping("/api/v1/product-requests")
public class ProductRequestControllerV1 {
    private final ProductUpdateRequestService productUpdateService;
    private final ProductSellRequestService productSellService;
    private final ProductRequestService requestService;

    @Autowired
    public ProductRequestControllerV1(ProductUpdateRequestService productUpdateService, ProductSellRequestService productSellService,
                                      ProductRequestService requestService) {
        this.productSellService = productSellService;
        this.productUpdateService = productUpdateService;
        this.requestService = requestService;
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductRequestsPairData> getAllRequests() {
        return ResponseEntity.ok(new ProductRequestsPairData(
           ProductRequestPreparer.prepareSellCollectionToResponse(productSellService.getAllPendingRequests()),
           ProductRequestPreparer.prepareUpdateCollectionToResponse(productUpdateService.getAllPendingRequests())
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<ProductRequestsPairData> getPersonalAllRequests(
            @AuthenticationPrincipal SimpleUserDetails userDetails
    ) {
        return ResponseEntity.ok(new ProductRequestsPairData(
                ProductRequestPreparer.prepareSellCollectionToResponse(productSellService.getAllRequestsBySubmitterId(userDetails.getId())),
                ProductRequestPreparer.prepareUpdateCollectionToResponse(productUpdateService.getAllRequestsBySubmitterId(userDetails.getId()))
        ));
    }

    @PostMapping("/me/sell")
    public ResponseEntity<Void> createPersonalSellRequest(
            @Valid @RequestBody ProductRequestCreateData data,
            @AuthenticationPrincipal SimpleUserDetails userDetails
    ) {
        productSellService.createRequest(
                new ProductSellRequestCreateParams(
                        userDetails.getId(),
                        data.name(),
                        data.price()
                )
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/update")
    public ResponseEntity<Void> createPersonalUpdateRequest(
            @Valid @RequestBody ProductRequestUpdateData data,
            @AuthenticationPrincipal SimpleUserDetails userDetails
    ) {
        productUpdateService.createRequest(new ProductUpdateRequestCreateParams(
                data.productId(),
                userDetails.getId(),
                data.name(),
                data.price()
        ));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> approveRequest(@PathVariable int id) {
        requestService.approveRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> rejectRequest(@PathVariable int id) {
        requestService.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }
}
