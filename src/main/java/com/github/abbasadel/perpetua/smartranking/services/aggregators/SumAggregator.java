package com.github.abbasadel.perpetua.smartranking.services.aggregators;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.AggregatorStrategy;

import java.util.List;

/**
 * A custom TimeSeries aggregator using the sum of the datapoints
 */
public class SumAggregator extends AggregatorStrategy {

    @Override
    public Double aggregate(List<TimeSeries> list) {
        return list.stream().mapToDouble(TimeSeries::getValue).sum();
    }
}