package ru.defix.coffeedelivery.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "users_roles", schema = "public", catalog = "coffee-delivery")
public class UsersRoles {
    @EmbeddedId
    private UsersRolesPK id;
}
