package ru.defix.coffeedelivery.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "product_requests", schema = "public", catalog = "coffee-delivery")
public class ProductRequest {
    public enum Type {
        SELL,
        UPDATE
    }

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id", referencedColumnName = "id")
    private User submitter;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
