package ru.defix.coffeedelivery.db.repository;

import ru.defix.coffeedelivery.db.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, Integer> {
}
