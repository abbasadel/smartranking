package com.github.abbasadel.perpetua.smartranking.services;

import com.github.abbasadel.perpetua.smartranking.services.providers.SellicsDataProvider;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private final SellicsDataProvider sellicsDataProvider;

    public RankingService(SellicsDataProvider sellicsDataProvider) {
        this.sellicsDataProvider = sellicsDataProvider;
    }

}
