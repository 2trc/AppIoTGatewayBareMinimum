package com.appiot.examples.gateway.samplegateway;

import java.util.logging.Level;
import java.util.logging.Logger;
import se.sigma.sensation.gateway.sdk.client.SensationClient;

/**
 * Created by eremtas on 10/19/2016.
 */
public class SampleGateway {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private SamplePlatform platform;
    private SensationClient sensationClient;

    public static void main(String[] args) {
        SampleGateway gateway = new SampleGateway();
        gateway.start();
    }

    private void start() {
        logger.log(Level.INFO, "Sample Gateway starting up.");
        platform = new SamplePlatform();
        sensationClient = new SensationClient(platform);
        sensationClient.start();
    }
}