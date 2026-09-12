package org.mnuykin.payment.service.repository;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BalanceRepository {
    Mono<BigDecimal> getBalance(String accountId);
    Mono<Void> executePayment(String accountId, BigDecimal amount);
}
