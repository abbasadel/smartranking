package com.github.abbasadel.perpetua.smartranking.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Models Time Series datapoint")
@Data
@AllArgsConstructor
public class TimeSeries {
    @Schema(description = "Timestamp of the datapoint",
            format = " YYYY-MM-DDThh:mi:ssZ",
            example = "2021-11-02T01:07:14Z")
    private Instant timestamp;

    @Schema(description = "Value of the datapoint")
    private double value;

    public static TimeSeries of(long timestamp, int value) {
        return new TimeSeries(Instant.ofEpochSecond(timestamp), value);
    }

    public static TimeSeries of(Map.Entry<Instant, Double> entry) {
        return new TimeSeries(entry.getKey(), entry.getValue());
    }
}
