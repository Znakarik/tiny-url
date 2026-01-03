package ru.znakarik.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.znakarik.db.model.url.jpa.DBConnector;
import ru.znakarik.db.model.url.jpa.DBConnectorImpl;
import ru.znakarik.repository.PostgreUrlRepository;
import ru.znakarik.repository.UrlRepository;
import ru.znakarik.repository.cache.MessagePublisher;
import ru.znakarik.repository.cache.MessagePublisherImpl;
import ru.znakarik.repository.cache.MessageSubscriber;
import ru.znakarik.repository.cache.RedisUrlRepository;
import ru.znakarik.service.*;

@Configuration
public class UrlConfiguration {

    @Bean
    public UrlService urlService(UrlRepository urlRepository,
                                 UrlGeneratorService urlGeneratorService) {
        return new UrlServiceImpl(urlRepository, urlGeneratorService);
    }

    @Bean
    public UrlAnalyticService urlAnalyticService(UrlRepository urlRepository) {
        return new UrlAnalyticServiceImpl(urlRepository);
    }

    @Bean
    public UrlRepository postgreUrlRepository(DBConnector jpaRepository) {
        return new PostgreUrlRepository(jpaRepository);
    }

    @Bean
    @Primary
    public UrlRepository redisUrlRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisUrlRepository(redisTemplate);
    }

    @Bean
    public UrlGeneratorService urlGeneratorService() {
        return new UrlGeneratorServiceImpl();
    }

    @Bean
    public DBConnector dbConnector() {
        return new DBConnectorImpl();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Настройка сериализатора для ключей
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Настройка JSON сериализатора для значений
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // Настройка ObjectMapper для корректной работы с LocalDateTime и другими типами
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }

    @Bean
    public MessagePublisher messagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        return new MessagePublisherImpl(redisTemplate, channelTopic);
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageSubscriber();
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageListener messageListener) {
        return new MessageListenerAdapter(messageListener);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }
}
