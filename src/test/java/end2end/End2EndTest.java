package end2end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import ru.znakarik.controller.analytic.GetUrlDataRs;
import ru.znakarik.controller.create.CreateUrlRs;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.repository.UrlRepository;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationEntryPoint.class)
@AutoConfigureMockMvc
public class End2EndTest {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void testGet() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/url/v1/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.urls.[0].shortUrl", Matchers.notNullValue()))
                .andExpect(jsonPath("$.urls.[0].longUrl", Matchers.notNullValue()));
    }

    @Test
    public void testCreate() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/url/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"longUrl\" : \"hi_from_UT\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", Matchers.is(0)))
                .andExpect(jsonPath("$.url.shortUrl", Matchers.startsWith("http://localhost:8080/url/v1/redirect/to?")))
                .andExpect(jsonPath("$.url.longUrl", Matchers.is("hi_from_UT")));
    }

    @Test
    public void testUpdateRedirect() throws Exception {
        // given
        String created = mvc.perform(MockMvcRequestBuilders.post("/url/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                String.format("    \"longUrl\" : \"hi_from_UT_%s\"\n", UUID.randomUUID()) +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CreateUrlRs createUrlRs = OBJECT_MAPPER.reader().readValue(created, CreateUrlRs.class);
        String shortUrl = createUrlRs.getUrl().getShortUrl();
        String urlId = createUrlRs.getUrl().getId();

        // when
        mvc.perform(MockMvcRequestBuilders.post("/url/v1/redirectToLongUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\n" +
                                "    \"shortUrl\" : \"%s\"\n" +
                                "}", shortUrl)))
                .andExpect(status().isOk());

        // when
        mvc.perform(MockMvcRequestBuilders.post("/url/v1/redirectToLongUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\n" +
                                "    \"shortUrl\" : \"%s\"\n" +
                                "}", shortUrl)))
                .andExpect(status().isOk());

        String contentAsString = mvc.perform(MockMvcRequestBuilders.post("/url/v1/analytic/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\n" +
                                "    \"shortUrl\" : \"%s\"\n," +
                                "    \"dateTo\" : \"2026-12-01\"\n," +
                                "    \"dateFrom\" : \"2024-11-01\"\n" +
                                "}", shortUrl)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GetUrlDataRs getUrlDataRs = OBJECT_MAPPER.reader().readValue(contentAsString, GetUrlDataRs.class);

        // then
        List<UrlRedirectPOJO> allRedirectsByUrlId = urlRepository.getAllRedirectsByUrlId(urlId);
        Assertions.assertEquals(2, allRedirectsByUrlId.size());

        Assertions.assertEquals(2, getUrlDataRs.getCount());
    }
}
