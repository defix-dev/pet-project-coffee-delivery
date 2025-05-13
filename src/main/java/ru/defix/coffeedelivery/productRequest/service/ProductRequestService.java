package ru.defix.coffeedelivery.productRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.coffeedelivery.db.entity.ProductRequest;
import ru.defix.coffeedelivery.db.repository.ProductRequestRepository;
import ru.defix.coffeedelivery.product.exception.ProductAlreadyExistsException;
import ru.defix.coffeedelivery.productRequest.exception.ProductRequestAlreadyExistsException;
import ru.defix.coffeedelivery.productRequest.exception.ProductRequestNotFoundException;
import ru.defix.coffeedelivery.productRequest.service.event.OnApproveProductRequestEvent;
import ru.defix.coffeedelivery.user.service.UserService;

import java.util.HashSet;
import java.util.Set;

/*  NOTE:
        Active request can be only ONE with status PENDING, requests with another type not count on counting.
*/

@Service
public class ProductRequestService {
    private final ProductRequestRepository requestRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductRequestService(ProductRequestRepository requestRepository, UserService userService,
                                 ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #submitterId == principal.id")
    public ProductRequest createRequest(ProductRequest.Type type, int submitterId) {
        if(existsPendingRequest(submitterId)) throw new ProductRequestAlreadyExistsException();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setStatus(ProductRequest.Status.PENDING);
        productRequest.setType(type);
        productRequest.setSubmitter(userService.getById(submitterId));

        requestRepository.save(productRequest);
        return productRequest;
    }

    private boolean existsPendingRequest(int submitterId) {
        return requestRepository.existsBySubmitter_IdAndStatus(submitterId, ProductRequest.Status.PENDING);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void approveRequest(int requestId) {
        ProductRequest request = getActiveRequestById(requestId);
        request.setStatus(ProductRequest.Status.APPROVED);

        eventPublisher.publishEvent(new OnApproveProductRequestEvent(request.getType(), request.getId()));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void rejectRequest(int requestId) {
        ProductRequest request = getActiveRequestById(requestId);
        request.setStatus(ProductRequest.Status.REJECTED);
    }

    private ProductRequest getActiveRequestById(int id) {
        return requestRepository.findActiveRequestById(id).orElseThrow(ProductRequestNotFoundException::new);
    }
}
