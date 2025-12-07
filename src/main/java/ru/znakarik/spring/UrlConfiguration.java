package ru.znakarik.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.znakarik.db.model.url.jpa.DBConnector;
import ru.znakarik.db.model.url.jpa.DBConnectorImpl;
import ru.znakarik.repository.PostgreUrlRepository;
import ru.znakarik.repository.UrlRepository;
import ru.znakarik.service.UrlGeneratorService;
import ru.znakarik.service.UrlGeneratorServiceImpl;
import ru.znakarik.service.UrlService;
import ru.znakarik.service.UrlServiceImpl;

@Configuration
public class UrlConfiguration {

    @Bean
    public UrlService urlService(UrlRepository urlRepository,
                                 UrlGeneratorService urlGeneratorService) {
        return new UrlServiceImpl(urlRepository, urlGeneratorService);
    }

    @Bean
    public UrlRepository urlRepository(DBConnector jpaRepository) {
        return new PostgreUrlRepository(jpaRepository);
    }

    @Bean
    public UrlGeneratorService urlGeneratorService() {
        return new UrlGeneratorServiceImpl();
    }

    @Bean
    public DBConnector dbConnector() {
        return new DBConnectorImpl();
    }
}
