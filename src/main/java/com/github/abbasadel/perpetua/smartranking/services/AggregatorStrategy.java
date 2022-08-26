package com.github.abbasadel.perpetua.smartranking.services;

import com.github.abbasadel.perpetua.smartranking.models.TimeSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * AggregatorStrategy is an abstract class that implements java Collector pattern
 * The purpose is to provide a custom implementation for steam processor to aggregate data
 */
public abstract class AggregatorStrategy implements Collector<TimeSeries, List<TimeSeries>, Double> {


    @Override
    public Supplier<List<TimeSeries>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<TimeSeries>, TimeSeries> accumulator() {
        return List::add;

    }

    @Override
    public BinaryOperator<List<TimeSeries>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<TimeSeries>, Double> finisher() {
        return this::aggregate;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }

    public abstract Double aggregate(List<TimeSeries> list);
}

