package ru.defix.coffeedelivery.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@jakarta.persistence.Table(name = "baskets", schema = "public", catalog = "coffee-delivery")
public class Basket {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "id")
    private int id;

    @Column(name = "quantity")
    private int quantity;

    @UpdateTimestamp
    @Column(name = "added_at")
    private Timestamp addedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Basket that = (Basket) o;

        if (id != that.id) return false;
        if (quantity != that.quantity) return false;
        if (!Objects.equals(addedAt, that.addedAt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + quantity;
        result = 31 * result + (addedAt != null ? addedAt.hashCode() : 0);
        return result;
    }
}
