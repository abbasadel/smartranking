package com.github.abbasadel.perpetua.smartranking.config;

import com.github.abbasadel.perpetua.smartranking.services.providers.sellicsClient.SellicsClient;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Here we put all the startup logic that needs to run
 * after Spring Context initialization
 */
@Component
@Log
public class Startup {
    private final SellicsClient sellicsClient;

    public Startup(SellicsClient sellicsClient) {
        this.sellicsClient = sellicsClient;
    }

    @PostConstruct
    public void init() {
        log.info("Running startup post construct");
        sellicsClient.download();
        log.info("Done running startup post construct");
    }

}
