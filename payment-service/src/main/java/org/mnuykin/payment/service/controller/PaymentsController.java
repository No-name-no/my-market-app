package org.mnuykin.payment.service.controller;

import org.mnuykin.server.api.PaymentApi;
import org.mnuykin.server.domain.BalanceResponse;
import org.mnuykin.server.domain.ExecuteRequest;
import org.mnuykin.server.domain.ExecuteResponse;
import org.mnuykin.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class PaymentsController implements PaymentApi {
    final private PaymentService paymentService;

    @Autowired
    PaymentsController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Override
    @PreAuthorize("hasAuthority('SERVICE')")
    public Mono<ResponseEntity<ExecuteResponse>> executePayment(String accountId, Mono<ExecuteRequest> executeRequest, ServerWebExchange exchange) {
        return paymentService.executePayment(accountId, executeRequest).map(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("hasAuthority('SERVICE')")
    public Mono<ResponseEntity<BalanceResponse>> getPaymentBalance(String accountId, ServerWebExchange exchange) {
        return paymentService.getBalance(accountId).map(ResponseEntity::ok);
    }
}
