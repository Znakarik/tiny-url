package ru.znakarik.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.repository.UrlRepository;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class UrlAnalyticServiceImpl implements UrlAnalyticService {
    private final UrlRepository urlRepository;

    @Override
    public int getAllRedirectsByShortUrlAndDate(String shortUrl, Date from, Date to) {
        UrlPOJO longUrlByShortUrl = urlRepository.findLongUrlByShortUrl(shortUrl)
                .orElseThrow(() -> new RuntimeException("Не найден url по shortUrl =" + shortUrl));
        List<UrlRedirectPOJO> allRedirectsByUrlId = urlRepository.getAllRedirectsByUrlId(longUrlByShortUrl.getId());
        CompareRq compareRq = new CompareRq.CompareRqBuilder()
                .from(from)
                .to(to)
                .build();

        return Math.toIntExact(allRedirectsByUrlId.stream()
                .map(UrlRedirectPOJO::getRedirectDate)
                .filter(date -> isDateBetween(date, compareRq))
                .count());
    }

    private boolean isDateBetween(Date comparable, CompareRq compareRq) {
        return comparable.after(compareRq.from) && comparable.before(compareRq.to);
    }

    @Builder
    private static class CompareRq {
        private final Date from;
        private final Date to;
    }
}
