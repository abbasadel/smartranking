package com.github.abbasadel.perpetua.smartranking.services.aggregators;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.AggregatorStrategy;

import java.util.List;

/**
 * A custom TimeSeries aggregator using median
 */
public class MedianAggregator extends AggregatorStrategy {

    @Override
    public Double aggregate(List<TimeSeries> list) {
        var sortedLit = list.stream().mapToDouble(TimeSeries::getValue).sorted();

        return (
                list.size() % 2 == 0 ?
                        sortedLit.skip((list.size() / 2) - 1).limit(2).average() :
                        sortedLit.skip(list.size() / 2).findFirst()
        ).getAsDouble();
    }
}