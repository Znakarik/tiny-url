package ru.znakarik.repository.cache;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.repository.UrlRepository;

import java.text.ParseException;
import java.util.*;

@Slf4j
public class RedisUrlRepository implements UrlRepository {
    private static final String URL_KEY = "Url";
    private static final String REDIRECTS_KEY = "Redirect";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    public RedisUrlRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Collection<UrlPOJO> getAllUrls() {
        Map<String, Object> entries = hashOperations.entries(URL_KEY);
        return entries.entrySet().stream()
                .map(entry -> fromRedisUrlToUrlPojo(entry.getKey(), (Map<String, Object>) entry.getValue()))
                .toList();
    }

    private UrlPOJO fromRedisUrlToUrlPojo(String id, Map<String, Object> obj) {
        long createDateTime = Long.parseLong(obj.get("createDateTime").toString());
        String shortUrl = obj.get("shortUrl").toString();
        String longUrl = obj.get("longUrl").toString();
        Integer redirects = 0;
        if (obj.get("redirects") != null) {
            redirects = Integer.parseInt(obj.get("redirects").toString());
        }
        return UrlPOJO.builder()
                .shortUrl(shortUrl)
                .id(id)
                .longUrl(longUrl)
                .createDateTime(new Date(createDateTime))
                .redirects(redirects)
                .build();
    }

    @Override
    public Optional<UrlPOJO> findUrlById(String id) {
        UrlPOJO urlPOJO = fromRedisUrlToUrlPojo(id, (Map<String, Object>) hashOperations.get(URL_KEY, id));
        return Optional.ofNullable(urlPOJO);
    }

    @Override
    public Optional<UrlPOJO> findLongUrlByShortUrl(String shortUrl) {
        return getAllUrls().stream()
                .filter(urlPOJO -> urlPOJO.getShortUrl().equals(shortUrl))
                .findFirst();
    }

    @Override
    public String create(String id, String longUrl, String shortUrl, String dateTime) {
        try {
            hashOperations.put(URL_KEY, id,
                    UrlPOJO.builder()
                            .longUrl(longUrl)
                            .shortUrl(shortUrl)
                            .createDateTime(UrlPOJO.DATE_FORMAT.parse(dateTime))
                            .build()
            );
            return id;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createNewRedirect(String redirectId, String urlId, String dateTime) {
        try {
            hashOperations.put(REDIRECTS_KEY, redirectId,
                    UrlRedirectPOJO.builder()
                            .urlId(urlId)
                            .id(redirectId)
                            .redirectDate(UrlPOJO.DATE_FORMAT.parse(dateTime))
                            .build());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UrlRedirectPOJO> getAllRedirectsByUrlId(String urlId) {
        return getAllRedirects().stream()
                .filter(urlRedirectPOJO -> urlRedirectPOJO.getUrlId().equals(urlId))
                .toList();
    }

    private UrlRedirectPOJO fromRedisRedirectToRedirectPojo(String redirectId, Map<String, Object> obj) {
        String urlId = (String) obj.get("urlId");
        long createDateTime = Long.parseLong(obj.get("redirectDate").toString());
        return UrlRedirectPOJO.builder()
                .id(redirectId)
                .urlId(urlId)
                .redirectDate(new Date(createDateTime))
                .build();
    }

    @Override
    public void deleteUrl(String id) {
        hashOperations.delete(URL_KEY, id);
    }

    @Override
    public void deleteRedirect(String redirectId) {
        hashOperations.delete(REDIRECTS_KEY, redirectId);
    }

    @Override
    public void clearUrls() {
        getAllUrls().forEach(urlPOJO -> deleteUrl(urlPOJO.getId()));
    }

    @Override
    public void clearRedirects() {
        getAllRedirects().forEach(urlRedirectPOJO -> deleteRedirect(urlRedirectPOJO.getId()));
    }

    @Override
    public Collection<UrlRedirectPOJO> getAllRedirects() {
        Map<String, Object> entries = hashOperations.entries(REDIRECTS_KEY);
        return entries.entrySet().stream()
                .map(entry -> fromRedisRedirectToRedirectPojo(entry.getKey(), (Map<String, Object>) entry.getValue()))
                .toList();
    }
}
