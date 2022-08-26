package com.github.abbasadel.perpetua.smartranking.controllers;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.Aggregator;
import com.github.abbasadel.perpetua.smartranking.services.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get a list time series containing the individual ranks for an ASIN, for a certain keyword",
            description = "Returns 202 if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timeseries list of all found datapoint")
    })
    @GetMapping(value = "/asin/{asin}", params = "keyword")
    Flux<TimeSeries> getTimeseriesRankingForAsinByKeyword(
            @PathVariable final String asin,
            @RequestParam final String keyword) {
        return rankingService.getTimeseriesRankingForAsinByKeyword(asin, keyword);
    }


    @Operation(
            summary = "Get a time series containing the aggregated ranks for all ASINs for a certain keyword",
            description = "Returns 202 if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timeseries list of all found datapoint"),
            @ApiResponse(responseCode = "400", description = "400 Bad Request if method is invalid")
    })
    @GetMapping(value = "/keyword/{keyword}")
    Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForKeyword(
            @PathVariable final String keyword,
            @RequestParam(name = "method") final Optional<Aggregator> aggregator) {
        return rankingService.getAggregatedTimeseriesRankingForKeyword(keyword, aggregator);
    }


    @Operation(
            summary = "Get a time series containing the aggregated ranks of all keywords for a certain ASIN",
            description = "Returns 202 if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timeseries list of all found datapoint"),
            @ApiResponse(responseCode = "400", description = "400 Bad Request if method is invalid")
    })
    @GetMapping(value = "/asin/{asin}")
    Mono<List<TimeSeries>> getAggregatedTimeseriesRankingForAsin(
            @PathVariable final String asin,
            @RequestParam(name = "method") final Optional<Aggregator> aggregator) {
        return rankingService.getAggregatedTimeseriesRankingForAsin(asin, aggregator);
    }

}
