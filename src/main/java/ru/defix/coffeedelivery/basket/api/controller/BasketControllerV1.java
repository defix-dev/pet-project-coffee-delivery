package ru.defix.coffeedelivery.basket.api.controller;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.basket.api.dto.response.BasketPersonalData;
import ru.defix.coffeedelivery.basket.api.util.BasketPreparer;
import ru.defix.coffeedelivery.basket.service.BasketService;
import ru.defix.coffeedelivery.basket.service.dto.BasketCreaseParams;

import java.util.List;

@RestController
@RequestMapping("/api/v1/baskets")
public class BasketControllerV1 {
    private final BasketService basketService;

    @Autowired
    public BasketControllerV1(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<BasketPersonalData>> getPersonalBaskets(@AuthenticationPrincipal SimpleUserDetails userDetails) {
        return ResponseEntity.ok(BasketPreparer.prepareBasketsToPersonalData(
                basketService.getAllByUserId(userDetails.getId())
        ));
    }

    @PostMapping("/me")
    public ResponseEntity<?> increasePersonalBasket(@AuthenticationPrincipal SimpleUserDetails userDetails,
                                                    @RequestParam int productId,
                                                    @RequestParam(required = false) Integer quantity) {
        basketService.increaseBasket(new BasketCreaseParams(
                userDetails.getId(),
                productId,
                quantity
        ));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> decreasePersonalBasket(@AuthenticationPrincipal SimpleUserDetails userDetails,
                                                    @RequestParam int productId,
                                                    @RequestParam(required = false) Integer quantity) {
        basketService.decreaseBasket(new BasketCreaseParams(
                userDetails.getId(),
                productId,
                quantity
        ));
        return ResponseEntity.noContent().build();
    }
}
