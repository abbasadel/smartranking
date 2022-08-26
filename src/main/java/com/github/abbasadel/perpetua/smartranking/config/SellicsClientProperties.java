package com.github.abbasadel.perpetua.smartranking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;


@ConfigurationProperties(prefix = "providers.sellics.s3")
@Data
public class SellicsClientProperties {
    private Region region = Region.EU_CENTRAL_1;
    private String accessKeyId;
    private String secretAccessKey;
    private String filename;
    private String bucket;

}