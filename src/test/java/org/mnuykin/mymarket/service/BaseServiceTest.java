package org.mnuykin.mymarket.service;

import org.mnuykin.mymarket.mapper.ItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderMapperImpl;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.mnuykin.mymarket.service.impl.OrderServiceImpl;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({CartServiceImpl.class, ItemServiceImpl.class, OrderServiceImpl.class,
        ItemMapperImpl.class, OrderMapperImpl.class, OrderItemMapperImpl.class})
@Sql("/test-data.sql")
@ActiveProfiles("test")
public abstract class BaseServiceTest {
    final protected Long id = 7L;
    final protected String title = "Камаз лимонов 1";
    final protected String description = "Это я - Камаз Эдичка";
    final protected String img_path = "limon.jpg";
    final protected Long price = 10000L;
}