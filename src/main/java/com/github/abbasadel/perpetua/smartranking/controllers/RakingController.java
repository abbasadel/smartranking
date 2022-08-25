package com.github.abbasadel.perpetua.smartranking.controllers;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.Aggregator;
import com.github.abbasadel.perpetua.smartranking.services.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/raking")
public class RakingController {
    private final RankingService rankingService;

    public RakingController(final RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping(value = "/asin/{asin}", params = "keyword")
    Flux<TimeSeries> getTimeseriesRankingForAsinByKeyword(
            @PathVariable final String asin,
            @RequestParam final String keyword) {
        return rankingService.getTimeseriesRankingForAsinByKeyword(asin, keyword);
    }

    @GetMapping(value = "/asin/{asin}")
    Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForAsin(
            @PathVariable final String asin,
            @RequestParam(name = "method") final Optional<Aggregator> aggregator) {
        return rankingService.getAggregatedTimeseriesRankingForAsin(asin, aggregator);
    }

    @GetMapping(value = "/keyword/{keyword}")
    Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForKeyword(
            @PathVariable final String keyword,
            @RequestParam(name = "method") final Optional<Aggregator> aggregator) {
        return rankingService.getAggregatedTimeseriesRankingForKeyword(keyword, aggregator);
    }

}
