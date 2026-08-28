package org.mnuykin.mymarket.service;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.mnuykin.mymarket.mapper.ItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderItemMapperImpl;
import org.mnuykin.mymarket.mapper.OrderMapperImpl;
import org.mnuykin.mymarket.service.impl.CartServiceImpl;
import org.mnuykin.mymarket.service.impl.ItemServiceImpl;
import org.mnuykin.mymarket.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;

@DataR2dbcTest
@Import({CartServiceImpl.class, ItemServiceImpl.class, OrderServiceImpl.class,
        ItemMapperImpl.class, OrderMapperImpl.class, OrderItemMapperImpl.class})
//@Sql("/test-data.sql")
@ActiveProfiles("test")
public abstract class BaseServiceTest {
    final protected Long id = 1000L;
    final protected String title = "Камаз лимонов 1";
    final protected String description = "Это я - Камаз Эдичка";
    final protected String img_path = "limon.jpg";
    final protected Long price = 10000L;
}