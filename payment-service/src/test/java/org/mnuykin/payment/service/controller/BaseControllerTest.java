package org.mnuykin.payment.service.controller;

import org.mnuykin.payment.service.config.SecurityConfig;
import org.mnuykin.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest({PaymentsController.class})
@AutoConfigureWebTestClient
@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected PaymentService paymentService;
}
