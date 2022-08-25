package com.github.abbasadel.perpetua.smartranking.controllers;

import com.github.abbasadel.perpetua.smartranking.config.SellicsClientProperties;
import com.github.abbasadel.perpetua.smartranking.services.RankingService;
import com.github.abbasadel.perpetua.smartranking.services.providers.SellicsDataProvider;
import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@Import({RankingService.class, SellicsDataProvider.class})
@WebFluxTest
class RakingControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    S3AsyncClient s3AsyncClient;

    @MockBean
    SellicsClientProperties sellicsClientProperties;

    @SpyBean
    SellicsClient sellicsClient;

    @InjectMocks
    SellicsDataProvider sellicsDataProvider;


    @BeforeEach
    void setUp() throws URISyntaxException {
        Path testDataLocation = Path.of(ClassLoader.getSystemResource("testdata/data-test.csv").toURI());

        //mock data
        Mockito.when(sellicsClient.getDataFileTempLocation()).thenReturn(testDataLocation);
    }

    @Test
    void shouldCallAsinEndpointAndReturnCorrectAggregatedResult() {
        Assertions.assertNotNull(webClient);
        Assertions.assertNotNull(sellicsClient);

        webClient.get()
                .uri("/v1/raking/asin/TESTASIN?method=MEDIAN")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].timestamp").value(equalTo(Instant.ofEpochSecond(1637024931).toString()))
                .jsonPath("$[0].value").value(equalTo(5.0));

    }
}