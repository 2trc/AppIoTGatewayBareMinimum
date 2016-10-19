package com.appiot.examples.gateway.samplegateway;

import com.appiot.examples.simulated.platform.device.HumiditySensor;
import com.appiot.examples.simulated.platform.device.SimulatedDevice;
import com.appiot.examples.simulated.platform.device.TemperatureSensor;
import se.sigma.sensation.gateway.sdk.client.core.SensationClientProperties;
import se.sigma.sensation.gateway.sdk.client.data.*;
import se.sigma.sensation.gateway.sdk.client.registry.SensorCollectionRegistration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by eremtas on 10/19/2016.
 */
public class AppIoTContract {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public static final int SIMULATED_GATEWAY_HARDWARE_TYPE_ID    = 12002;
    public static final int SIMULATED_DEVICE_HARDWARE_TYPE_ID     = 10001;
    public static final int SENSOR_TYPE_ID_TEMPERATURE        = 1;
    public static final int SENSOR_TYPE_ID_HUMIDITY       = 2;

    public static int getSensorHardwareTypeId(String simulatedPlatformIdentifier) {
        if(simulatedPlatformIdentifier != null) {
            if(simulatedPlatformIdentifier.equals(TemperatureSensor.TEMP)) {
                return SENSOR_TYPE_ID_TEMPERATURE;
            } else if(simulatedPlatformIdentifier.equals(HumiditySensor.HUM)) {
                return SENSOR_TYPE_ID_HUMIDITY;
            }
        }
        return -1;
    }

    public DataCollectorStatus updateDataCollectorStatus() {
        logger.log(Level.INFO, "called");
        DataCollectorStatus status = new DataCollectorStatus();
        // This must be the same id as the gateway hardware type id in
        // Application Platform for IoT
        status.setHardwareTypeId(AppIoTContract.SIMULATED_GATEWAY_HARDWARE_TYPE_ID);
        status.setStatus(DataCollectorStatusCode.OK);
        return status;
    }

    public RestartApplicationResponseCode restartApplication() {
        logger.log(Level.INFO, "called");
        // Insert code for restarting the application on your gateway
        return RestartApplicationResponseCode.OK;
    }

    public RebootResponseCode reboot() {
        logger.log(Level.INFO, "called");
        // Insert code for reboot your gateway
        return RebootResponseCode.OK;
    }

    /*public void updateDataCollectorSettings(SensationClientProperties properties) {
        logger.log(Level.INFO, "called");
        String sendInterval = properties.getSetting("sendInterval", "1000");
        logger.log(Level.INFO, String.format("Setting sendInterval to %s", sendInterval));
        manager.setSendInterval(Integer.parseInt(sendInterval));
    }

    public void reportDiscoveredSensorCollections(String correlationId) {
        logger.log(Level.INFO, "called");
        List<SimulatedDevice> devices = manager.getDevices();
        for(SimulatedDevice device : devices) {
            DiscoveredSensorCollection discoveredSensorCollection = new DiscoveredSensorCollection();
            // Unix time in milliseconds UTC
            discoveredSensorCollection.setLastObserved(System.currentTimeMillis());
            discoveredSensorCollection.setSerialNumber(device.getSerialNumber());
            // This would be the actual dB or any QoS indicator of the radio communication.
            discoveredSensorCollection.setSignalStrength(-50);
            client.reportDiscoveredSensorCollection(correlationId, discoveredSensorCollection);
        }
    }

    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationCreated(SensorCollectionRegistration registration) {
        logger.log(Level.INFO, "called");
        // Connect / Start listening for measurements from registered device.
        logger.log(Level.INFO, registration.getSerialNumber());
        // Apply settings
        logger.log(Level.INFO, registration.getSettings().toString());
        return SensorCollectionRegistrationResponseCode.ADD_OK;
    }

    public SensorCollectionRegistration updateSensorCollectionStatus(SensorCollectionRegistration registration) {
        logger.log(Level.INFO, "called");
        registration.setStatus(200);
        registration.setFirmwareVersion("1.0");
        registration.setSoftwareVersion("1.0");
        registration.setHardwareVersion("1.0");
        return registration;
    }

    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationUpdated(SensorCollectionRegistration registration) {
        logger.log(Level.INFO, "called");
        // Apply settings to the device
        return SensorCollectionRegistrationResponseCode.ADD_OK;
    }

    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationDeleted(SensorCollectionRegistration registration) {
        logger.log(Level.INFO, "called");
        // Disconnect / Stop listening for measurements from registered device.
        SimulatedDevice device = manager.getDeviceBySerialNumber(registration.getSerialNumber());
        device.stop();
        return SensorCollectionRegistrationResponseCode.DELETE_OK;
    }*/

}