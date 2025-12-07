package ru.znakarik.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.znakarik.controller.create.CreateUrlRq;
import ru.znakarik.controller.create.CreateUrlRs;
import ru.znakarik.controller.get.all.GetAllRs;
import ru.znakarik.controller.get.single.GetLongUrlRq;
import ru.znakarik.controller.get.single.GetLongUrlRs;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.service.UrlService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("url/v1")
@Slf4j
public class UrlController {
    private final UrlService urlService;

    @PostMapping(value = "/create", produces = "application/json")
    public CreateUrlRs create(@RequestBody CreateUrlRq createUrlRq) {
        UrlPOJO urlPOJO = urlService.create(createUrlRq.getLongUrl());
        log.info("created URL = " + urlPOJO);
        return CreateUrlRs.builder()
                .url(Url.builder()
                        .id(urlPOJO.getId())
                        .shortUrl(urlPOJO.getShortUrl())
                        .longUrl(urlPOJO.getLongUrl())
                        .build())
                .build();
    }

    // когда клиент переходит по нашей короткой ссылке
    // он должен попасть в контроллер, который сделает запишет
    // в репо редиктов новую ссылку и сделает редирект
    // -> отправит клиенту редирект ?

    @PostMapping("/redirectToLongUrl")
    public GetLongUrlRs getLongUrl(@RequestBody GetLongUrlRq getLongUrlRq) {
        UrlPOJO url = urlService.getByShortUrl(getLongUrlRq.getShortUrl());
        if (url != null) {
            urlService.updateUrlRedirect(url.getId());
            return GetLongUrlRs.builder()
                    .url(Url.builder()
                            .longUrl(url.getLongUrl())
                            .id(url.getId())
                            .shortUrl(url.getShortUrl())
                            .build())
                    .build();
        }
        throw new RuntimeException("Не найден URL по " + getLongUrlRq.getShortUrl());
    }

    @GetMapping("/getAll")
    @ResponseBody
    public GetAllRs getAllUrls() {
        List<UrlPOJO> all = urlService.getAll();
        return GetAllRs.builder()
                        .urls(all.stream()
                                .map(this::fromPojoToUrl)
                                .collect(Collectors.toList())
                        )
                        .build();
    }

    private Url fromPojoToUrl(UrlPOJO urlPOJO) {
        return Url.builder()
                .shortUrl(urlPOJO.getShortUrl())
                .longUrl(urlPOJO.getLongUrl())
                .id(urlPOJO.getId())
                .build();
    }
}
