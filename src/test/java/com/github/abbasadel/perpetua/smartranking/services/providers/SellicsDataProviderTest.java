package com.github.abbasadel.perpetua.smartranking.services.providers;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URISyntaxException;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class SellicsDataProviderTest {

    SellicsDataProvider provider;
    SellicsClient sellicsClient;

    @BeforeEach
    void setUp() throws URISyntaxException {
        SellicsClient sellicsClient = Mockito.spy(new SellicsClient(null, null));
        Path testDataLocation = Path.of(ClassLoader.getSystemResource("testdata/data-test.csv").toURI());

        //mock data
        Mockito.when(sellicsClient.getDataFileTempLocation()).thenReturn(testDataLocation);
    }

    @Test
    void shouldFetchDataCorrectlyFromTheClient() {
        //given
        provider = new SellicsDataProvider(sellicsClient);

        //when
        Flux<TimeSeries> result = provider.findRakingByAsin("TESTASIN");

        //then
        StepVerifier.create(result)
                .expectNext(TimeSeries.of(1637024931, 91))
                .expectNextCount(18)
                .expectComplete()
                .verify();

    }
}