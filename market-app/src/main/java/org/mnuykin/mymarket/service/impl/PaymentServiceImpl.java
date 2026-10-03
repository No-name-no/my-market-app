package org.mnuykin.mymarket.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mnuykin.client.api.PaymentApi;
import org.mnuykin.client.domain.ExecuteRequest;
import org.mnuykin.client.domain.ExecuteResponse;
import org.mnuykin.mymarket.advice.exception.PaymentServiceUnavailableException;
import org.mnuykin.mymarket.config.security.SecurityContextUtils;
import org.mnuykin.mymarket.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentApi paymentApi;

    @Autowired
    PaymentServiceImpl(PaymentApi paymentApi){
        this.paymentApi = paymentApi;
    }

    @Override
    public Mono<Long> getBalance() {
        return SecurityContextUtils.getCurrentUsername().flatMap(
                userName -> paymentApi.getPaymentBalance(userName).map(balanceResponseResponseEntity -> {
                    assert Objects.requireNonNull(balanceResponseResponseEntity.getBody()).getBalance() != null;
                    return balanceResponseResponseEntity.getBody().getBalance().toBigInteger().longValue();
                }).onErrorResume(e -> {
                    log.error("Failed to get balance for user {}", userName, e);
                    return Mono.error(new PaymentServiceUnavailableException(e.getMessage(), e));
                })
        );
    }

    @Override
    public Mono<Boolean> pay(Long amount) {
        return SecurityContextUtils.getCurrentUsername().flatMap(
                userName -> paymentApi.executePayment(userName, Mono.just(new ExecuteRequest(BigDecimal.valueOf(amount))))
                        .timeout(Duration.ofSeconds(5))
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                        .map(executeResponseResponseEntity -> {
                            assert executeResponseResponseEntity.getBody() != null;
                            return executeResponseResponseEntity.getBody().getStatus() == ExecuteResponse.StatusEnum.SUCCESSFUL;
                    }).onErrorResume(e -> {
                        log.error("Failed to execute payment for user {} amount {}", userName, amount, e);
                        return Mono.error(new PaymentServiceUnavailableException(e.getMessage(), e));
                    })
        );
    }
}