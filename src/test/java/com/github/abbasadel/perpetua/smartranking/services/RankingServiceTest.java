package com.github.abbasadel.perpetua.smartranking.services;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.providers.SellicsDataProvider;
import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    RankingService rankingService;
    SellicsDataProvider sellicsDataProvider;
    SellicsClient sellicsClient;


    @BeforeEach
    void setUp() throws URISyntaxException {
        SellicsClient sellicsClient = Mockito.spy(new SellicsClient(null, null));
        sellicsDataProvider = new SellicsDataProvider(sellicsClient);
        rankingService = new RankingService(sellicsDataProvider);

        Path testDataLocation = Path.of(ClassLoader.getSystemResource("testdata/data-test.csv").toURI());

        //mock data
        Mockito.when(sellicsClient.getDataFileTempLocation()).thenReturn(testDataLocation);
    }

    @Test
    void shouldAggregateDataCorrectlyByAsin() {
        //given
        double sumOfAsin = 100;
        double meanOfAsin = 5.263;
        double medianOfAsin = 5.0;

        //then
        List<TimeSeries> result = rankingService.getAggregatedTimeseriesRankingForAsin("TESTASIN", Optional.of(Aggregator.SUM)).log().block();
        assertEquals(sumOfAsin, result.stream().findFirst().get().getValue());

        result = rankingService.getAggregatedTimeseriesRankingForAsin("TESTASIN", Optional.of(Aggregator.MEAN)).log().block();
        assertEquals(meanOfAsin, result.stream().findFirst().get().getValue(), 0.001);

        result = rankingService.getAggregatedTimeseriesRankingForAsin("TESTASIN", Optional.of(Aggregator.MEDIAN)).log().block();
        assertEquals(medianOfAsin, result.stream().findFirst().get().getValue());

    }
}