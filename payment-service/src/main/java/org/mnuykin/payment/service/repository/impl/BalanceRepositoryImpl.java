package org.mnuykin.payment.service.repository.impl;

import org.mnuykin.payment.service.repository.BalanceRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BalanceRepositoryImpl implements BalanceRepository {
    private final BigDecimal initBalance = BigDecimal.valueOf(20_000L);
    private final Map<String, BigDecimal> accountBalance;

    public BalanceRepositoryImpl() {
        accountBalance = new ConcurrentHashMap<>();
    }

    private BigDecimal ensureBalance(String accountId){
        return accountBalance.containsKey(accountId)
                ? accountBalance.get(accountId)
                : accountBalance.put(accountId, initBalance);
    }

    @Override
    public Mono<BigDecimal> getBalance(String accountId) {
        return Mono.fromCallable(() -> ensureBalance(accountId));
    }

    @Override
    public Mono<Void> executePayment(String accountId, BigDecimal amount) {
        return Mono.fromRunnable(() -> {
            if (amount == null || amount.signum() <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }

            BigDecimal balance = ensureBalance(accountId);
            if(balance.compareTo(amount) < 0){
                throw new RuntimeException("Insufficient funds");
            }

            accountBalance.put(accountId,  balance.subtract(amount));
        });
    }
}
