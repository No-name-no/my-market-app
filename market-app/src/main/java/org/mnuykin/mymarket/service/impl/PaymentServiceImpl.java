package org.mnuykin.mymarket.service.impl;

import org.mnuykin.client.api.PaymentApi;
import org.mnuykin.client.domain.ExecuteRequest;
import org.mnuykin.client.domain.ExecuteResponse;
import org.mnuykin.mymarket.advice.exception.PaymentServiceUnavailableException;
import org.mnuykin.mymarket.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final String ACCOUNT_ID = "VERY_NICE_ACCOUNT";
    private final PaymentApi paymentApi;

    @Autowired
    PaymentServiceImpl(PaymentApi paymentApi,
                       WebClient.Builder webClientBuilder,
                       @Value("${payment.service.url}") String serviceUrl){
        this.paymentApi = paymentApi;
    }

    @Override
    public Mono<Long> getBalance() {
        return paymentApi.getPaymentBalance(ACCOUNT_ID).map(balanceResponseResponseEntity -> {
            assert Objects.requireNonNull(balanceResponseResponseEntity.getBody()).getBalance() != null;
            return balanceResponseResponseEntity.getBody().getBalance().toBigInteger().longValue();
        }).onErrorResume(e -> {
            e.printStackTrace();
            return Mono.error(new PaymentServiceUnavailableException(e.getMessage(), e));
        });
    }

    @Override
    public Mono<Boolean> pay(Long amount) {
        return paymentApi.executePayment(ACCOUNT_ID, Mono.just(new ExecuteRequest(BigDecimal.valueOf(amount))))
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .map(executeResponseResponseEntity -> {
                    assert executeResponseResponseEntity.getBody() != null;
                    return executeResponseResponseEntity.getBody().getStatus() == ExecuteResponse.StatusEnum.SUCCESSFUL;
                }).onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(false);
                });
    }
}