package com.example.cache.config;

import com.example.cache.domain.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Redis 에 User 객체를 저장하고 조회할 때 사용됩니다.
     * */
    @Bean
    RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        /**
        * JSON 직렬화/역직렬화를 위한 Jackson 의 ObjectMapper 객체를 생성합니다.
		* FAIL_ON_UNKNOWN_PROPERTIES 설정을 false 로 하여, JSON 에 예상하지 못한 필드가 있을 때 오류가 발생하지 않도록 합니다.
		* JavaTimeModule 을 등록하여 LocalDate, LocalDateTime 같은 Java 8 날짜 및 시간 클래스를 적절히 직렬화할 수 있도록 합니다.
		* WRITE_DATE_KEYS_AS_TIMESTAMPS 를 비활성화하여 날짜를 타임스탬프가 아닌 ISO 8601 형식으로 직렬화합니다.
        * */
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        var template = new RedisTemplate<String, User>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        /**
         * Redis 에 데이터를 직렬화/역직렬화하여 저장할 수 있도록 설정
         * */
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));
        return template;
    }

    @Bean
    RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory connectionFactory) {
        /**
         * 다형성 직렬화/역직렬화를 위한 검증기를 생성합니다.
         * Object.class를 기준으로 다형성을 허용하여 Object 타입의 다양한 서브타입을 처리할 수 있게 합니다.
         * */
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(Object.class)
                .build();
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }
}
