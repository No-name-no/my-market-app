package org.mnuykin.payment.service.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mnuykin.payment.service.service.PaymentService;
import org.mnuykin.server.domain.BalanceResponse;
import org.mnuykin.server.domain.ExecuteRequest;
import org.mnuykin.server.domain.ExecuteResponse;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class PaymentControllerTest extends BaseControllerTest{
    @Autowired
    private PaymentService paymentService;

    @Test
    void executePaymentDecline(){
        String accountId = "test_acct_id";
        ExecuteRequest executeRequest = new ExecuteRequest(BigDecimal.ONE);
        Mockito.when(paymentService.executePayment(eq(accountId), Mockito.<Mono<ExecuteRequest>>any()))
                .thenReturn(Mono.just(new ExecuteResponse().status(ExecuteResponse.StatusEnum.REJECTED)));

        webTestClient.post().uri("/payment/execute/{accountId}", accountId).
                body(BodyInserters.fromValue(executeRequest))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody(ExecuteResponse.class).value(
                        executeResponse-> {
                            assert executeResponse != null;
                            Assertions.assertSame(ExecuteResponse.StatusEnum.REJECTED, executeResponse.getStatus());
                        }
                );


        verify(paymentService, times(1)).executePayment(eq(accountId), any());
        verifyNoMoreInteractions(paymentService);
    }

    @Test
    void getPaymentBalance(){
        String accountId = "test_acct_id";
        BigDecimal balance = BigDecimal.ONE;
        Mockito.when(paymentService.getBalance(accountId))
                .thenReturn(Mono.just(new BalanceResponse().balance(balance)));

        webTestClient.get().uri("/payment/balance/{accountId}", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody(BalanceResponse.class).value(
                        balanceResponse-> {
                            assert balanceResponse != null;
                            Assertions.assertSame(balance, balanceResponse.getBalance());
                        }
                );


        verify(paymentService, times(1)).getBalance(accountId);
        verifyNoMoreInteractions(paymentService);
    }
}