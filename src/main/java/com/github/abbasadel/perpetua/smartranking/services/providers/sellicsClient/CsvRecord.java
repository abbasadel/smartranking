package com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient;

import lombok.Data;

@Data
public class CsvRecord {
    final long timestamp;
    final String asin;
    final String keywords;
    final short rank;

    public static CsvRecord of(String[] values) {
        if (values.length != 4) {
            throw new IllegalArgumentException("Invalid array size, should be 4 and not " + values.length);
        }

        return new CsvRecord(
                Long.parseLong(values[0]),
                values[1],
                values[2],
                Short.parseShort(values[3])
        );
    }

    CsvRecord(long timestamp, String asin, String keywords, short rank) {
        this.timestamp = timestamp;
        this.asin = asin;
        this.keywords = keywords;
        this.rank = rank;
    }
}
