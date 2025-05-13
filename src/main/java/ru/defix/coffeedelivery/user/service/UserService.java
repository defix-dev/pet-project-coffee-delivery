package ru.defix.coffeedelivery.user.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.defix.coffeedelivery.db.entity.User;
import ru.defix.coffeedelivery.db.repository.UserRepository;
import ru.defix.coffeedelivery.user.exception.EmailAlreadyExistsException;
import ru.defix.coffeedelivery.user.exception.UserNotFoundException;
import ru.defix.coffeedelivery.user.exception.UsernameAlreadyExistsException;
import ru.defix.coffeedelivery.user.service.dto.UserSaveParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void save(UserSaveParams params) {
        if(userRepository.findByUsername(params.username()).isPresent()) throw new UsernameAlreadyExistsException();
        if(userRepository.findByEmail(params.email()).isPresent()) throw new EmailAlreadyExistsException();

        User user = new User();
        user.setEmail(params.email());
        user.setUsername(params.username());
        user.setPassword(encoder.encode(params.password()));
        user.setRoles(Set.of(User.getDefaultRole()));

        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN') or #username == principal.username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
