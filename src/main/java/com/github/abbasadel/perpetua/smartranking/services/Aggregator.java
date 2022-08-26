package com.github.abbasadel.perpetua.smartranking.services;

import com.github.abbasadel.perpetua.smartranking.services.aggregators.MeanAggregator;
import com.github.abbasadel.perpetua.smartranking.services.aggregators.MedianAggregator;
import com.github.abbasadel.perpetua.smartranking.services.aggregators.SumAggregator;

/**
 * Aggregator enum contains a list of all classes that implements AggregatorStrategy
 * to support aggregating Time series data
 */
public enum Aggregator {
    MEAN(new MeanAggregator()),
    MEDIAN(new MedianAggregator()),
    SUM(new SumAggregator());

    private final AggregatorStrategy aggregatorStrategy;

    Aggregator(AggregatorStrategy aggregatorStrategy) {
        this.aggregatorStrategy = aggregatorStrategy;
    }

    public AggregatorStrategy getAggregatorStrategy() {
        return aggregatorStrategy;
    }
}