package org.mnuykin.mymarket.config;

import org.mnuykin.client.api.PaymentApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PaymentClientConfig {
    @Value("${payment.service.url}")
    private String serviceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public PaymentApi paymentApi(WebClient.Builder builder) {
        WebClient client = builder.baseUrl(serviceUrl).build();
        WebClientAdapter adapter = WebClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(PaymentApi.class);
    }
}