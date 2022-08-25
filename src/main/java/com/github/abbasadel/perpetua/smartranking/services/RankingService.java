package com.github.abbasadel.perpetua.smartranking.services;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.providers.SellicsDataProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final SellicsDataProvider sellicsDataProvider;

    public RankingService(SellicsDataProvider sellicsDataProvider) {
        this.sellicsDataProvider = sellicsDataProvider;
    }

    public Flux<TimeSeries> getTimeseriesRankingForAsinByKeyword(final String asin, final String keyword) {
        return sellicsDataProvider.findRakingByAsinAndKeyword(asin, keyword);
    }

    public Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForAsin(final String asin, final Optional<Aggregator> aggregator) {
        var result = sellicsDataProvider.findRakingByAsin(asin);
        return aggregateResult(result, aggregator);
    }

    public Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForKeyword(final String keyword, final Optional<Aggregator> aggregator) {
        var result = sellicsDataProvider.findRakingByKeyword(keyword);
        return aggregateResult(result, aggregator);
    }

    private Mono<List<TimeSeries>> aggregateResult(final Flux<TimeSeries> ts, final Optional<Aggregator> aggregator) {
        if (aggregator.isPresent()) {
            Collector<TimeSeries, ?, Map<Instant, Double>> collector = Collectors.groupingBy(
                    TimeSeries::getTimestamp,
                    aggregator.get().getAggregatorStrategy());
            return ts.collect(collector)
                    .map(m -> m.entrySet().stream().map(TimeSeries::of).collect(Collectors.toList()));
        }

        return ts.collect(Collectors.toList());
    }
}
