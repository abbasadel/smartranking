package com.github.abbasadel.perpetua.smartranking.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
public class TimeSeries {
    private Instant timestamp;
    private double value;

    public static TimeSeries of(long timestamp, int value) {
        return new TimeSeries(Instant.ofEpochSecond(timestamp), value);
    }

    public static TimeSeries of(Map.Entry<Instant, Double> entry) {
        return new TimeSeries(entry.getKey(), entry.getValue());
    }
}
