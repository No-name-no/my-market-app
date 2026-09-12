package org.mnuykin.mymarket.repository;

import org.mnuykin.mymarket.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> getUsersByLogin(String login);
}
