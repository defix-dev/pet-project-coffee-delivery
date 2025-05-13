package ru.defix.coffeedelivery.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "product_sell_requests", schema = "public", catalog = "coffee-delivery")
public class ProductSellRequest {
    @Id
    @Column(name = "request_id")
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ProductRequest productRequest;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;
}
