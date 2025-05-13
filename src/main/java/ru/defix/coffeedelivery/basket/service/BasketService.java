package ru.defix.coffeedelivery.basket.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.coffeedelivery.basket.exception.BasketNotFoundException;
import ru.defix.coffeedelivery.basket.service.dto.BasketCreaseParams;
import ru.defix.coffeedelivery.db.entity.Basket;
import ru.defix.coffeedelivery.db.repository.BasketRepository;
import ru.defix.coffeedelivery.product.service.ProductService;
import ru.defix.coffeedelivery.user.service.UserService;

import java.util.List;
import java.util.function.Function;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public BasketService(BasketRepository basketRepository, UserService userService,
                         ProductService productService) {
        this.basketRepository = basketRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN') or #creaseParams.userId() == principal.id")
    @Transactional
    public void increaseBasket(BasketCreaseParams creaseParams) {
        int quantity = normalizeQuantity(creaseParams.quantity());
        if(quantity <= 0) return;
        Function<Basket, Integer> quantityIfExists = basket -> creaseParams.quantity() == null ? basket.getQuantity() + 1 : creaseParams.quantity() + basket.getQuantity();
        basketRepository.save(basketRepository.findByUser_IdAndProduct_Id(creaseParams.userId(), creaseParams.productId())
                .map(basket -> { basket.setQuantity(quantityIfExists.apply(basket)); return basket; })
                .orElseGet(() -> {
                    Basket basket = new Basket();
                    basket.setUser(userService.getById(creaseParams.userId()));
                    basket.setProduct(productService.getById(creaseParams.productId()));
                    basket.setQuantity(quantity);
                    return basket;
                }));
    }

    @PreAuthorize("hasRole('ADMIN') or #creaseParams.userId() == principal.id")
    @Transactional
    public void decreaseBasket(BasketCreaseParams creaseParams) {
        Basket basket = getByUserIdAndProductId(creaseParams.userId(), creaseParams.productId());
        int quantityToDecrease = normalizeQuantity(creaseParams.quantity());
        int quantity = basket.getQuantity() - quantityToDecrease;
        if(quantity <= 0) {
            basketRepository.delete(basket);
            return;
        }
        basket.setQuantity(quantity);
        basketRepository.save(basket);
    }

    private int normalizeQuantity(Integer quantity) {
        return quantity == null ? 1 : quantity;
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public List<Basket> getAllByUserId(int userId) {
        return basketRepository.findAllByUser_Id(userId);
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.user.id == principal.id")
    public Basket getById(int id) {
        return basketRepository.findById(id).orElseThrow(BasketNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public Basket getByUserIdAndProductId(int userId, int productId) {
        return basketRepository.findByUser_IdAndProduct_Id(userId, productId).orElseThrow(BasketNotFoundException::new);
    }
}
