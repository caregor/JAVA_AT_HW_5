package ru.gb.accu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetLocationTest extends AbstractTest{

    private static final Logger logger = LoggerFactory.getLogger(GetLocationTest.class);

    @Test
    void get_shloudReturn200() throws IOException, URISyntaxException {
        logger.info("Тест код 200 запущен.");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Location bodyOk = new Location();
        bodyOk.setKey("OK");

        Location bodyError = new Location();
        bodyError.setKey("Error");

        logger.debug("Формирование мока для GET /locations/v1/293142");
        stubFor(get(urlPathEqualTo("/locations/v1/293142"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http client Создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/293142");
        URI uriOk = new URIBuilder(request.getURI()).build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);
        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/293142")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), Location.class).getKey());
    }
}
