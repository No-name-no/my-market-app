package org.mnuykin.payment.service.controller;

import org.mnuykin.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest({PaymentsController.class})
public abstract class BaseControllerTest {
    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected PaymentService paymentService;
}
