package ru.znakarik.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.service.UrlService;

@AllArgsConstructor
@RestController
@RequestMapping("url/v1/redirect")
@Slf4j
public class RedirectController {
    @Autowired
    private final UrlService urlService;

    @GetMapping("/to")
    @ResponseBody
    public RedirectView redirect(@RequestParam("id") String id) {
        String idd = id.substring(id.lastIndexOf("=") + 1);
        log.info("received rq with id = " + idd);
        UrlPOJO urlPOJO = urlService.getById(idd);
        urlService.updateUrlRedirect(idd);
        return new RedirectView(urlPOJO.getLongUrl());
    }
}
