package org.mnuykin.mymarket.config.security;

import org.jspecify.annotations.NullMarked;
import org.mnuykin.mymarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@NullMarked
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public ReactiveUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.getUsersByLogin(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Пользователь не найден: " + username)))
                .map(user -> User.builder()
                        .username(user.getLogin())
                        .password(user.getPassword())
                        .disabled(false)
                        .roles("USER")
                        .build()
                );
    }
}
