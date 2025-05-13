package ru.defix.coffeedelivery.auth.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.defix.coffeedelivery.auth.service.dto.SimpleUserDetails;
import ru.defix.coffeedelivery.db.entity.Role;
import ru.defix.coffeedelivery.db.entity.User;
import ru.defix.coffeedelivery.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new SimpleUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList());
    }
}
