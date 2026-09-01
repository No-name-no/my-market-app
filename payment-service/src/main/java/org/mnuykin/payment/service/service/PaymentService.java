package org.mnuykin.payment.service.service;

import org.mnuykin.server.domain.BalanceResponse;
import org.mnuykin.server.domain.ExecuteRequest;
import org.mnuykin.server.domain.ExecuteResponse;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ExecuteResponse> executePayment (String accountId, Mono<ExecuteRequest> executeRequest);
    Mono<BalanceResponse> getBalance (String accountId);
}
