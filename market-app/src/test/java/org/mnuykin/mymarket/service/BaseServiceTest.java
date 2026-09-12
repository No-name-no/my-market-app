package org.mnuykin.mymarket.service;

import org.mnuykin.client.api.PaymentApi;
import org.mnuykin.mymarket.mapper.ItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderMapperImpl;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.mnuykin.mymarket.service.impl.OrderServiceImpl;
import org.mnuykin.mymarket.service.impl.PaymentServiceImpl;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataR2dbcTest
@Import({CartServiceImpl.class, ItemServiceImpl.class, OrderServiceImpl.class,
        PaymentServiceImpl.class,
        ItemMapperImpl.class, OrderMapperImpl.class, OrderItemMapperImpl.class})
@ActiveProfiles("test")
public abstract class BaseServiceTest {
    @MockitoBean
    protected CacheService cacheService;

    @MockitoBean
    protected PaymentApi paymentApi;

    final protected Long id = 1000L;
    final protected String title = "Камаз лимонов 1";
    final protected String description = "Это я - Камаз Эдичка";
    final protected String img_path = "limon.jpg";
    final protected Long price = 10000L;
}