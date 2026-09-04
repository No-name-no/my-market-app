package org.mnuykin.mymarket.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataJackson3Configuration;
import org.springframework.data.web.config.SpringDataWebSettings;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;

@Configuration
public class CacheConfig {
    final static public Duration CACHE_TTL = Duration.ofMinutes(3);

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        final SpringDataWebSettings settings = new SpringDataWebSettings(EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO);
        ObjectMapper objectMapper = JsonMapper.builder().activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(),
                        DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                ).addModule(new SpringDataJackson3Configuration.PageModule(settings)).build();

        final JacksonJsonRedisSerializer<Object> jsonSerializer = new JacksonJsonRedisSerializer<>(objectMapper, Object.class);

        final StringRedisSerializer stringSerializer = new StringRedisSerializer();

        final RedisSerializationContext<String, Object> serializationContext =
                RedisSerializationContext.<String, Object>newSerializationContext(stringSerializer)
                        .key(stringSerializer)
                        .value(jsonSerializer)
                        .hashKey(stringSerializer)
                        .hashValue(jsonSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }
}
