package org.mnuykin.payment.service.service;

import org.junit.jupiter.api.Test;
import org.mnuykin.payment.service.service.impl.PaymentServiceImpl;
import org.mnuykin.server.domain.BalanceResponse;
import org.mnuykin.server.domain.ExecuteRequest;
import org.mnuykin.server.domain.ExecuteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import({PaymentServiceImpl.class})
@ActiveProfiles("test")
@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    PaymentService paymentService;

    @Test
    void test(){
        String testAccount = "TEST_ACCT_VAL";

        BalanceResponse balanceResponse = paymentService.getBalance(testAccount).block();
        assertNotNull(balanceResponse);
        assertNotNull(balanceResponse.getBalance());

        ExecuteResponse rs = paymentService.executePayment(
                testAccount, Mono.just(new ExecuteRequest().amount(balanceResponse.getBalance()))
        ).block();

        assertNotNull(rs);
        assertEquals(ExecuteResponse.StatusEnum.SUCCESSFUL, rs.getStatus());
        assertEquals(BigDecimal.ZERO, rs.getRemainingBalance());

        rs = paymentService.executePayment(
                testAccount, Mono.just(new ExecuteRequest().amount(balanceResponse.getBalance()))
        ).block();
        assertNotNull(rs);
        assertEquals(ExecuteResponse.StatusEnum.INSUFFICIENT_FUNDS, rs.getStatus());

        rs = paymentService.executePayment(
                testAccount, Mono.just(new ExecuteRequest().amount(BigDecimal.TEN.negate()))
        ).block();
        assertNotNull(rs);
        assertEquals(ExecuteResponse.StatusEnum.REJECTED, rs.getStatus());
    }
}
