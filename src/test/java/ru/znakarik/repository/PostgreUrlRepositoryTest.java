package ru.znakarik.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.db.model.url.jpa.DBConnector;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostgreUrlRepositoryTest {

    private DBConnector dbConnector;
    private PostgreUrlRepository repository;

    @BeforeEach
    void setUp() {
        dbConnector = mock(DBConnector.class);
        repository = new PostgreUrlRepository(dbConnector);
    }

    @Test
    void getAllUrls_returnsUrls() throws SQLException {
        UrlPOJO url = UrlPOJO.builder().build(); // настройте по необходимости
        when(dbConnector.execute(anyString(), any())).thenReturn(List.of(url));

        Collection<UrlPOJO> result = repository.getAllUrls();

        assertEquals(1, result.size());
        assertTrue(result.contains(url));
        verify(dbConnector).execute(eq("SELECT * FROM urls;"), any());
    }

    @Test
    void findUrlById_existingId_returnsUrl() throws SQLException {
        UrlPOJO url = UrlPOJO.builder().build();
        when(dbConnector.execute(anyString(), any())).thenReturn(url);

        Optional<UrlPOJO> result = repository.findUrlById("123");

        assertTrue(result.isPresent());
        assertEquals(url, result.get());
        verify(dbConnector).execute(contains("123"), any());
    }

    @Test
    void findUrlById_nonExistingId_returnsEmpty() throws SQLException {
        when(dbConnector.execute(anyString(), any())).thenReturn(null);

        Optional<UrlPOJO> result = repository.findUrlById("123");

        assertTrue(result.isEmpty());
    }

    @Test
    void findLongUrlByShortUrl_returnsUrl() throws SQLException {
        UrlPOJO url = UrlPOJO.builder().build();
        when(dbConnector.execute(anyString(), any())).thenReturn(url);

        Optional<UrlPOJO> result = repository.findLongUrlByShortUrl("short");

        assertTrue(result.isPresent());
        assertEquals(url, result.get());
        verify(dbConnector).execute(contains("short"), any());
    }

    @Test
    void create_insertsUrlAndReturnsId() throws SQLException {
        String id = "id1";
        String longUrl = "https://long.url";
        String shortUrl = "short";
        String dateTime = "2026-01-04T12:00";

        when(dbConnector.execute(anyString(), any())).thenReturn(null);

        String result = repository.create(id, longUrl, shortUrl, dateTime);

        assertEquals(id, result);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(dbConnector).execute(captor.capture(), any());
        assertTrue(captor.getValue().contains(id));
        assertTrue(captor.getValue().contains(longUrl));
        assertTrue(captor.getValue().contains(shortUrl));
    }

    @Test
    void createNewRedirect_insertsRedirect() throws SQLException {
        when(dbConnector.execute(anyString(), any())).thenReturn(null);

        repository.createNewRedirect("rid", "uid", "2026-01-04T12:00");

        verify(dbConnector).execute(contains("rid"), any());
    }

    @Test
    void getAllRedirectsByUrlId_returnsRedirects() throws SQLException {
        UrlRedirectPOJO redirect = UrlRedirectPOJO.builder().build();
        when(dbConnector.execute(anyString(), any())).thenReturn(List.of(redirect));

        List<UrlRedirectPOJO> result = repository.getAllRedirectsByUrlId("uid");

        assertEquals(1, result.size());
        assertEquals(redirect, result.get(0));
    }

    @Test
    void clearUrls_executesTruncate() throws SQLException {
        when(dbConnector.execute(anyString(), any())).thenReturn(null);

        repository.clearUrls();

        verify(dbConnector).execute(eq("TRUNCATE TABLE urls;"), any());
    }

    @Test
    void clearRedirects_executesTruncate() throws SQLException {
        when(dbConnector.execute(anyString(), any())).thenReturn(null);

        repository.clearRedirects();

        verify(dbConnector).execute(eq("TRUNCATE TABLE url_redirects;"), any());
    }

    @Test
    void deleteUrl_throwsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> repository.deleteUrl("id"));
        assertEquals("Not implemented yet", exception.getMessage());
    }

    @Test
    void deleteRedirect_throwsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> repository.deleteRedirect("rid"));
        assertEquals("Not implemented yet", exception.getMessage());
    }

    @Test
    void getAllRedirects_throwsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, repository::getAllRedirects);
        assertEquals("Not implemented yet", exception.getMessage());
    }
}