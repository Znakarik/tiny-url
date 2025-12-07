package ru.znakarik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.znakarik.ApplicationEntryPoint;
import ru.znakarik.controller.create.CreateUrlRs;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.repository.UrlRepository;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationEntryPoint.class)
@AutoConfigureMockMvc
class RedirectControllerTest {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UrlRepository urlRepository;


    @Test
    public void testUpdateRedirect() throws Exception {
        // given
        String rs = mvc.perform(MockMvcRequestBuilders.post("/url/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"longUrl\" : \"https://vk.com/\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CreateUrlRs createUrlRs = OBJECT_MAPPER.reader().readValue(rs, CreateUrlRs.class);

        Assertions.assertEquals(createUrlRs.getUrl().getLongUrl(), "https://vk.com/");
        // when
        rs = mvc.perform(MockMvcRequestBuilders.get("/url/v1/redirect/to?id=" + createUrlRs.getUrl().getShortUrl()))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("https://vk.com/"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        rs = mvc.perform(MockMvcRequestBuilders.get("/url/v1/redirect/to?id=" + createUrlRs.getUrl().getShortUrl()))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("https://vk.com/"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        // then
        List<UrlRedirectPOJO> allRedirectsByUrlId = urlRepository.getAllRedirectsByUrlId(createUrlRs.getUrl().getId());
        Assertions.assertEquals(allRedirectsByUrlId.size(), 2);
    }
}