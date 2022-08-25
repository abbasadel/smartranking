package com.github.abbasadel.perpetua.smartranking.services.providers;

import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import org.springframework.stereotype.Service;

@Service
public class SellicsDataProvider {

    public SellicsDataProvider(final SellicsClient sellicsClient) {
        this.sellicsClient = sellicsClient;
    }

    private final SellicsClient sellicsClient;

}
