package ru.znakarik.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.repository.UrlRepository;

import java.util.*;

@AllArgsConstructor
@Slf4j
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    private final UrlGeneratorService urlGeneratorService;

    @Override
    public UrlPOJO create(String longUrl) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String shortUrl = urlGeneratorService.createShortUrl(id);
        String createTime = UrlPOJO.DATE_FORMAT.format(new Date());
        urlRepository.create(id, longUrl, shortUrl, createTime);

        log.info("created new url [{}] with id = {}", longUrl, id);
        Optional<UrlPOJO> urlById = urlRepository.findUrlById(id);
        return urlById.orElseThrow(() -> new RuntimeException("Не удалось создать новый URL для длинного URL = " + longUrl));
    }

    @Override
    public List<UrlPOJO> getAll() {
        Collection<UrlPOJO> all = urlRepository.getAllUrls();
        return all.stream().toList();
    }

    @Override
    public UrlPOJO updateUrlRedirect(String urlId) {
        // проверить, что такой url существует
        Optional<UrlPOJO> url = urlRepository.findUrlById(urlId);
        if (url.isPresent()) {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            String createTime = UrlPOJO.DATE_FORMAT.format(new Date());

            urlRepository.createNewRedirect(id, url.get().getId(), createTime);
            // сделать инкремент перехода
            return url.orElse(null);
        } else {
            throw new RuntimeException("Не можем создать редирект, так как не нашли записи url = " + urlId);
        }
    }

    @Override
    public UrlPOJO getByShortUrl(String shortUrl) {
        return urlRepository.findLongUrlByShortUrl(shortUrl)
                .orElseThrow(() -> new RuntimeException("Не найден url по shortUrl =" + shortUrl));
    }

    @Override
    public UrlPOJO getById(String id) {
        return urlRepository.findUrlById(id).orElseThrow();
    }
}
