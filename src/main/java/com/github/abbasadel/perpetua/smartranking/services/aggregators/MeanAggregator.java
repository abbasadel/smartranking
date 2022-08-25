package com.github.abbasadel.perpetua.smartranking.services.aggregators;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;
import com.github.abbasadel.perpetua.smartranking.services.AggregatorStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class MeanAggregator extends AggregatorStrategy {

    @Override
    public Double aggregate(List<TimeSeries> list) {
        return list.stream().collect(Collectors.averagingDouble(TimeSeries::getValue));
    }
}
