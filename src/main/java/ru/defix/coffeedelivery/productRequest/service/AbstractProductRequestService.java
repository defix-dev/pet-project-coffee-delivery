package ru.defix.coffeedelivery.productRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.defix.coffeedelivery.db.entity.ProductRequest;

import java.util.Set;

public abstract class AbstractProductRequestService<RT, CP> {
    private final ProductRequestService requestService;

    protected AbstractProductRequestService(ProductRequestService requestService) {
        this.requestService = requestService;
    }

    public abstract Set<RT> getAllPendingRequests();
    public abstract Set<RT> getAllRequestsBySubmitterId(int submitterId);
    public abstract RT getById(int requestId);

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #params.submitterId() == principal.id")
    public void createRequest(CP params) {
        savePayload(requestService.createRequest(
                getRequestType(),
                extractSubmitterId(params)
        ), params);
    }

    protected abstract ProductRequest.Type getRequestType();
    protected abstract int extractSubmitterId(CP params);
    protected abstract void savePayload(ProductRequest productRequest, CP params);
}
