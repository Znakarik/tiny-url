package ru.znakarik.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.znakarik.controller.analytic.GetUrlDataRq;
import ru.znakarik.controller.analytic.GetUrlDataRs;
import ru.znakarik.service.UrlAnalyticService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@RestController
@RequestMapping("url/v1/analytic")
public class UrlAnalyticController {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public UrlAnalyticService urlService;
    // получить кол-во переходов по урлу:
    //   - за все время
    //   - за период

    @PostMapping(value = "get", produces = "application/json")
    public GetUrlDataRs getUrlData(@RequestBody GetUrlDataRq getUrlDataRq) throws ParseException {
        // получим кол-во переходов по урлу
         // сервис должен:
         // 1. получать все переходы по урлу (по дефолту - за все время)
         // 2. получать все переходы по урлу за дату
        Date from = DATE_FORMAT.parse(getUrlDataRq.getDateFrom());
        Date to = DATE_FORMAT.parse(getUrlDataRq.getDateTo());
        int allRedirectsByShortUrlAndDate = urlService.getAllRedirectsByShortUrlAndDate(getUrlDataRq.getShortUrl(), from, to);

        return GetUrlDataRs.builder()
                .count(allRedirectsByShortUrlAndDate)
                .build();
    }
}
