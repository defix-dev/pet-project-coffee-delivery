package ru.defix.coffeedelivery.productRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.coffeedelivery.db.entity.ProductRequest;
import ru.defix.coffeedelivery.db.entity.ProductSellRequest;
import ru.defix.coffeedelivery.db.entity.ProductUpdateRequest;
import ru.defix.coffeedelivery.db.repository.ProductSellRequestRepository;
import ru.defix.coffeedelivery.db.repository.ProductUpdateRequestRepository;
import ru.defix.coffeedelivery.product.service.ProductService;
import ru.defix.coffeedelivery.productRequest.exception.ProductSellRequestNotFoundException;
import ru.defix.coffeedelivery.productRequest.exception.ProductUpdateRequestNotFoundException;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductSellRequestCreateParams;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductUpdateRequestCreateParams;
import ru.defix.coffeedelivery.productRequest.service.event.OnApproveProductRequestEvent;
import ru.defix.coffeedelivery.productRequest.service.event.OnSellProductRequestEvent;
import ru.defix.coffeedelivery.productRequest.service.event.OnUpdateProductRequestEvent;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductUpdateRequestService
        extends AbstractProductRequestService<ProductUpdateRequest, ProductUpdateRequestCreateParams> {
    private final ProductUpdateRequestRepository updateRequestRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductService productService;

    @Autowired
    public ProductUpdateRequestService(ProductUpdateRequestRepository updateRequestRepository,
                                     ProductRequestService requestService,
                                     ApplicationEventPublisher eventPublisher,
                                       ProductService productService) {
        super(requestService);
        this.updateRequestRepository = updateRequestRepository;
        this.eventPublisher = eventPublisher;
        this.productService = productService;
    }

    @EventListener
    public void onApproveProductRequest(OnApproveProductRequestEvent event) {
        if (event.requestType() != ProductRequest.Type.UPDATE) return;
        ProductUpdateRequest updateRequest = getById(event.requestId());
        eventPublisher.publishEvent(new OnUpdateProductRequestEvent(
                updateRequest.getProduct().getId(),
                updateRequest.getName(),
                updateRequest.getPrice()
        ));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public Set<ProductUpdateRequest> getAllPendingRequests() {
        return updateRequestRepository.findAllPending();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or #submitterId == principal.id")
    public Set<ProductUpdateRequest> getAllRequestsBySubmitterId(int submitterId) {
        return updateRequestRepository.findAllByProductRequest_Submitter_Id(submitterId);
    }

    @Override
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or returnObject.productRequest.submitter.id == principal.id")
    public ProductUpdateRequest getById(int requestId) {
        return updateRequestRepository.findById(requestId).orElseThrow(ProductUpdateRequestNotFoundException::new);
    }

    @Override
    protected ProductRequest.Type getRequestType() {
        return ProductRequest.Type.UPDATE;
    }

    @Override
    protected int extractSubmitterId(ProductUpdateRequestCreateParams params) {
        return params.submitterId();
    }

    @Override
    protected void savePayload(ProductRequest productRequest, ProductUpdateRequestCreateParams params) {
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setProductRequest(productRequest);
        updateRequest.setName(params.name());
        updateRequest.setPrice(params.price());
        updateRequest.setProduct(productService.getById(params.productId()));

        updateRequestRepository.save(updateRequest);
    }
}

