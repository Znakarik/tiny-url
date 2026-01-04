package ru.znakarik.repository.cache;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;

import java.text.DateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RedisUrlRepositoryTest {

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private RedisUrlRepository repository;
    private static final DateFormat DATE_FORMAT = UrlPOJO.DATE_FORMAT;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        repository = new RedisUrlRepository(redisTemplate);
        repository.init();
    }

    @Test
    void getAllUrls_returnsUrls() {
        String id = "1";
        Map<String, Object> redisEntry = Map.of(
                "shortUrl", "s1",
                "longUrl", "http://long.url",
                "createDateTime", String.valueOf(new Date().getTime()),
                "redirects", "2"
        );
        Map<String, Object> redisMap = Map.of(id, redisEntry);
        when(hashOperations.entries("Url")).thenReturn(redisMap);

        Collection<UrlPOJO> urls = repository.getAllUrls();

        assertEquals(1, urls.size());
        UrlPOJO url = urls.iterator().next();
        assertEquals("s1", url.getShortUrl());
        assertEquals("http://long.url", url.getLongUrl());
        assertEquals(2, url.getRedirects());
        assertEquals(id, url.getId());
    }

    @Test
    void findUrlById_returnsOptional() {
        String id = "1";
        Map<String, Object> redisEntry = Map.of(
                "shortUrl", "s1",
                "longUrl", "http://long.url",
                "createDateTime", String.valueOf(new Date().getTime())
        );
        when(hashOperations.get("Url", id)).thenReturn(redisEntry);

        Optional<UrlPOJO> urlOpt = repository.findUrlById(id);

        assertTrue(urlOpt.isPresent());
        assertEquals("s1", urlOpt.get().getShortUrl());
    }

    @Test
    void findLongUrlByShortUrl_returnsCorrectUrl() {
        UrlPOJO url1 = UrlPOJO.builder().shortUrl("s1").id("1").longUrl("http://long1").build();
        UrlPOJO url2 = UrlPOJO.builder().shortUrl("s2").id("2").longUrl("http://long2").build();

        RedisUrlRepository spyRepo = spy(repository);
        doReturn(List.of(url1, url2)).when(spyRepo).getAllUrls();

        Optional<UrlPOJO> result = spyRepo.findLongUrlByShortUrl("s2");

        assertTrue(result.isPresent());
        assertEquals("2", result.get().getId());
    }

    @Test
    void create_savesUrl() throws Exception {
        String id = "1";
        String longUrl = "http://long.url";
        String shortUrl = "s1";
        String date = DATE_FORMAT.format(new Date());

        String returnedId = repository.create(id, longUrl, shortUrl, date);

        assertEquals(id, returnedId);
        ArgumentCaptor<UrlPOJO> captor = ArgumentCaptor.forClass(UrlPOJO.class);
        verify(hashOperations).put(eq("Url"), eq(id), captor.capture());
        assertEquals(longUrl, captor.getValue().getLongUrl());
        assertEquals(shortUrl, captor.getValue().getShortUrl());
    }

    @Test
    void createNewRedirect_savesRedirect() throws Exception {
        String redirectId = "r1";
        String urlId = "u1";
        String date = DATE_FORMAT.format(new Date());

        repository.createNewRedirect(redirectId, urlId, date);

        ArgumentCaptor<UrlRedirectPOJO> captor = ArgumentCaptor.forClass(UrlRedirectPOJO.class);
        verify(hashOperations).put(eq("Redirect"), eq(redirectId), captor.capture());
        assertEquals(urlId, captor.getValue().getUrlId());
    }

    @Test
    void deleteUrl_callsDelete() {
        repository.deleteUrl("id1");
        verify(hashOperations).delete("Url", "id1");
    }

    @Test
    void deleteRedirect_callsDelete() {
        repository.deleteRedirect("rid1");
        verify(hashOperations).delete("Redirect", "rid1");
    }

    @Test
    void clearUrls_deletesAllUrls() {
        UrlPOJO url = UrlPOJO.builder().id("1").build();
        RedisUrlRepository spyRepo = spy(repository);
        doReturn(List.of(url)).when(spyRepo).getAllUrls();

        spyRepo.clearUrls();

        verify(spyRepo).deleteUrl("1");
    }

    @Test
    void clearRedirects_deletesAllRedirects() {
        UrlRedirectPOJO redirect = UrlRedirectPOJO.builder().id("r1").build();
        RedisUrlRepository spyRepo = spy(repository);
        doReturn(List.of(redirect)).when(spyRepo).getAllRedirects();

        spyRepo.clearRedirects();

        verify(spyRepo).deleteRedirect("r1");
    }

    @Test
    void getAllRedirects_returnsRedirects() {
        String redirectId = "r1";
        Map<String, Object> redisEntry = Map.of(
                "urlId", "u1",
                "redirectDate", String.valueOf(new Date().getTime())
        );
        Map<String, Object> redisMap = Map.of(redirectId, redisEntry);
        when(hashOperations.entries("Redirect")).thenReturn(redisMap);

        Collection<UrlRedirectPOJO> redirects = repository.getAllRedirects();

        assertEquals(1, redirects.size());
        UrlRedirectPOJO redirect = redirects.iterator().next();
        assertEquals("u1", redirect.getUrlId());
        assertEquals(redirectId, redirect.getId());
    }
}