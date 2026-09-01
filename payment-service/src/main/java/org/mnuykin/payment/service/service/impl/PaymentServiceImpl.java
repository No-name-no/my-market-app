package org.mnuykin.payment.service.service.impl;

import org.mnuykin.server.domain.BalanceResponse;
import org.mnuykin.server.domain.ExecuteRequest;
import org.mnuykin.server.domain.ExecuteResponse;
import org.mnuykin.payment.service.repository.BalanceRepository;
import org.mnuykin.payment.service.service.PaymentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentServiceImpl implements PaymentService {
    final private BalanceRepository balanceRepository;

    PaymentServiceImpl(BalanceRepository balanceRepository){
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Mono<ExecuteResponse> executePayment(String accountId, Mono<ExecuteRequest> executeRequest) {
        return executeRequest.flatMap(executeRequest1 ->
                balanceRepository.executePayment(accountId, executeRequest1.getAmount())
                        .then(balanceRepository.getBalance(accountId)
                                .map(balance -> new ExecuteResponse()
                                        .status(ExecuteResponse.StatusEnum.SUCCESSFUL)
                                        .remainingBalance(balance)
                        )
                )
        ).onErrorResume(throwable -> throwable instanceof RuntimeException
                ? Mono.just(new ExecuteResponse().status(ExecuteResponse.StatusEnum.INSUFFICIENT_FUNDS))
                : Mono.just(new ExecuteResponse().status(ExecuteResponse.StatusEnum.REJECTED))
        );
    }

    @Override
    public Mono<BalanceResponse> getBalance(String accountId) {
        return balanceRepository.getBalance(accountId)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .map(bigDecimal -> new BalanceResponse().balance(bigDecimal));
    }
}