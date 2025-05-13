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
import ru.defix.coffeedelivery.db.repository.ProductSellRequestRepository;
import ru.defix.coffeedelivery.productRequest.exception.ProductSellRequestNotFoundException;
import ru.defix.coffeedelivery.productRequest.service.dto.ProductSellRequestCreateParams;
import ru.defix.coffeedelivery.productRequest.service.event.OnApproveProductRequestEvent;
import ru.defix.coffeedelivery.productRequest.service.event.OnSellProductRequestEvent;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductSellRequestService
        extends AbstractProductRequestService<ProductSellRequest, ProductSellRequestCreateParams> {
    private final ProductSellRequestRepository sellRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductSellRequestService(ProductSellRequestRepository sellRequestRepository,
                                     ProductRequestService requestService,
                                     ApplicationEventPublisher eventPublisher) {
        super(requestService);
        this.sellRequestRepository = sellRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void onApproveProductRequest(OnApproveProductRequestEvent event) {
        if (event.requestType() != ProductRequest.Type.SELL) return;
        ProductSellRequest sellRequest = getById(event.requestId());
        eventPublisher.publishEvent(new OnSellProductRequestEvent(
                sellRequest.getProductRequest().getSubmitter().getId(),
                sellRequest.getName(),
                sellRequest.getPrice()
        ));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public Set<ProductSellRequest> getAllPendingRequests() {
        return sellRequestRepository.findAllPending();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or #submitterId == principal.id")
    public Set<ProductSellRequest> getAllRequestsBySubmitterId(int submitterId) {
        return sellRequestRepository.findAllByProductRequest_Submitter_Id(submitterId);
    }

    @Override
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or returnObject.productRequest.submitter.id == principal.id")
    public ProductSellRequest getById(int id) {
        return sellRequestRepository.findById(id).orElseThrow(ProductSellRequestNotFoundException::new);
    }

    @Override
    protected ProductRequest.Type getRequestType() {
        return ProductRequest.Type.SELL;
    }

    @Override
    protected int extractSubmitterId(ProductSellRequestCreateParams params) {
        return params.submitterId();
    }

    @Override
    protected void savePayload(ProductRequest productRequest, ProductSellRequestCreateParams params) {
        ProductSellRequest sellRequest = new ProductSellRequest();
        sellRequest.setProductRequest(productRequest);
        sellRequest.setName(params.name());
        sellRequest.setPrice(params.price());

        sellRequestRepository.save(sellRequest);
    }
}
