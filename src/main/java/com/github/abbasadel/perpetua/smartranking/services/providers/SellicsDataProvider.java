package com.github.abbasadel.perpetua.smartranking.services.providers;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Service
public class SellicsDataProvider {

    public SellicsDataProvider(final SellicsClient sellicsClient) {
        this.sellicsClient = sellicsClient;
    }

    private final SellicsClient sellicsClient;

    public Flux<TimeSeries> findRakingByAsin(final String asin) {
        return findRakingByAsinAndKeyword(asin, null);
    }

    public Flux<TimeSeries> findRakingByKeyword(final String keyword) {
        return findRakingByAsinAndKeyword(null, keyword);
    }

    public Flux<TimeSeries> findRakingByAsinAndKeyword(final String asin, final String keyword) {
        var dataStream = sellicsClient.fetchAllRecords();
        if (StringUtils.hasText(asin)) {
            dataStream = dataStream.filter(r -> asin.equals(r.getAsin()));
        }

        if (StringUtils.hasText(keyword)) {
            dataStream = dataStream.filter(r -> r.getKeywords().contains(keyword));
        }

        return dataStream.map(r -> TimeSeries.of(r.getTimestamp(), r.getRank()));
    }
}
