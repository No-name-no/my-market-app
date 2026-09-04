package org.mnuykin.mymarket.service;

import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Long> getBalance();
    Mono<Boolean> pay(Long amount);
}