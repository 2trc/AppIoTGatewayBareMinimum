package com.appiot.examples.gateway.samplegateway;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.appiot.examples.simulated.platform.SimulatedPlatformListener;
import com.appiot.examples.simulated.platform.SimulatedPlatformManager;
import com.appiot.examples.simulated.platform.device.HumiditySensor;
import com.appiot.examples.simulated.platform.device.SensorData;
import com.appiot.examples.simulated.platform.device.SimulatedDevice;
import se.sigma.sensation.gateway.sdk.client.Platform;
import se.sigma.sensation.gateway.sdk.client.PlatformInitialisationException;
import se.sigma.sensation.gateway.sdk.client.SensationClient;
import se.sigma.sensation.gateway.sdk.client.core.SensationClientProperties;
import se.sigma.sensation.gateway.sdk.client.data.DataCollectorDeleteResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.DataCollectorStatus;
import se.sigma.sensation.gateway.sdk.client.data.ISensorMeasurement;
import se.sigma.sensation.gateway.sdk.client.data.NetworkSetting;
import se.sigma.sensation.gateway.sdk.client.data.NetworkSettingResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.RebootResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.RestartApplicationResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.SensorCollectionRegistrationResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.UpdatePackage;
import se.sigma.sensation.gateway.sdk.client.data.UpdatePackageResponseCode;
import se.sigma.sensation.gateway.sdk.client.data.DataCollectorStatusCode;
import se.sigma.sensation.gateway.sdk.client.registry.SensorCollectionRegistration;
import se.sigma.sensation.gateway.sdk.client.registry.SensorCollectionRegistry;

/**
 * Created by eremtas on 10/19/2016.
 */
public class SamplePlatform implements Platform {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private SimulatedPlatformManager manager;

    /**
     * Called when measurements are sent to Sensation telling if sensing the
     * measurements was successful or not.
     *
     * @param measurementsSent
     *            - A list of measurements with acknowledge parameter set.
     */
    public void acknowledgeMeasurementsSent(List<ISensorMeasurement> arg0) {
        logger.log(Level.INFO, "called");
    }

    /**
     * Called from Sensation requesting the data collector to add wifi setting.
     *
     * @param networkSetting
     *            the wifi setting to add.
     * @return NetworkSettingResponseCode
     */
    public NetworkSettingResponseCode addNetworkSetting(NetworkSetting arg0) {
        logger.log(Level.INFO, "called");
        return NetworkSettingResponseCode.OK;
    }

    /**
     * Called from Sensation requesting the data collector to be deleted.
     *
     * @param forceDelete
     *            - If true, ignore errors.
     * @return DataCollectorDeleteResponseCode
     */
    public DataCollectorDeleteResponseCode deleteDataCollector(boolean arg0) {
        logger.log(Level.INFO, "called");
        return DataCollectorDeleteResponseCode.OK;
    }

    /**
     * Called from Sensation when using custom commands.
     *
     * @param correlationId
     *            - Id to identify this request, use id when responding back to
     *            Sensation.
     * @param actorId
     *            - The id of the microservice in Sensation.
     * @param payloadType
     *            - Indicates what kind of payload.
     * @param payload
     *            - The payload handle.
     */
    public void handleCustomCommand(String arg0, String arg1, String arg2, String arg3) {
        logger.log(Level.INFO, "called");
    }

    /**
     * Initializes the platform.
     *
     * @param client
     *            - SensationClient to be used by the platform.
     * @throws PlatformInitialisationException
     *             - Indicates platform initialization failure.
     */

    public void init(final SensationClient sensationClient) throws PlatformInitialisationException {
        logger.log(Level.INFO, "called");
        manager = new SimulatedPlatformManager();

        logger.log(Level.INFO, "Setting up devices.");
        manager.addDevice("Device 1", "111111");
        manager.addDevice("Device 2", "111122");
        manager.addDevice("Device 3", "111133");

        // If registered, start up the device
        for(SimulatedDevice device : manager.getDevices()) {
            //System.out.println("Device serial number: " + device.getSerialNumber());

            if(sensationClient.isSerialNumberRegistered(device.getSerialNumber())) {
                device.start();
            }
        }

        manager.addListener(new SimulatedPlatformListener() {
            public void onData(List<SensorData> measurements) {
                for(SensorData sensorData : measurements) {
                    logger.log(Level.INFO, String.format("New Measurement from device %s %s %s",
                            sensorData.getSerialNumber(),
                            sensorData.getSensorType(),
                            sensorData.getValue()));
                }
            }
        });

        logger.log(Level.INFO, "Starting up SimulatedPlatformManager...");
        manager.start();
        logger.log(Level.INFO, "Done.");

        //
        // Set interval in milliseconds between sending measurements to listener.
        manager.setSendInterval(2500);

        // List registered devices
        List<SimulatedDevice> devices = manager.getDevices();

        // Get registered device with serial number 1.
        SimulatedDevice device = manager.getDeviceBySerialNumber("111111");

        // Starts the device and measurements from sensors are generated.
        device.start();

        // Stops the device.
        device.stop();

        // Get the humidity sensor of device 1
        HumiditySensor humiditySensor = device.getHumiditySensor();

        // Set interval in milliseconds between generating measurements
        humiditySensor.setReportInterval(500);

        // Get interval in milliseconds between generating measurements
        humiditySensor.getReportInterval();
    }

    /**
     * Called from Sensation requesting the system to reboot.
     *
     * @return RebootResponseCode
     */
    public RebootResponseCode reboot() {
        logger.log(Level.INFO, "called");
        return RebootResponseCode.OK;
    }

    /**
     * Request from Sensation to report sensor collections in range of the data
     * collector. The platform is expected to report sensor collections
     * available.
     *
     * @param correlationId
     *            - The id of the request, use when answering.
     * @see DiscoveredSensorCollection.
     * @see SensationClient.reportDicoveredSensorCollection().
     */
    public void reportDiscoveredSensorCollections(String arg0) {
        logger.log(Level.INFO, "called");
    }

    /**
     * Called from Sensation requesting the application to restart.
     *
     * @return RestartApplicationResponseCode
     */
    public RestartApplicationResponseCode restartApplication() {
        logger.log(Level.INFO, "called");
        return RestartApplicationResponseCode.OK;
    }

    /**
     * Called when a sensor collection has been registered. The Sensor
     * Collection is already registered in the SensorCollectionRegistry. This
     * call is for the platform to act if necessary.
     *
     * @see SensorCollectionRegistry
     * @param registration
     *            - A reference to the entry in SensorCollectionRegistry.
     * @return SensorCollectionRegistrationResponseCode
     */
    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationCreated(
            SensorCollectionRegistration arg0) {
        logger.log(Level.INFO, "called");
        return SensorCollectionRegistrationResponseCode.ADD_OK;
    }

    /**
     * Called when a sensor collection that is already registered has been
     * removed. The Sensor Collection is already removed in the
     * SensorCollectionRegistry. This call is for the platform to act if
     * necessary.
     *
     * @see SensorCollectionRegistry
     * @param registration
     *            - A reference to the entry in SensorCollectionRegistry.
     * @return SensorCollectionRegistrationResponseCode - Indicating
     */
    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationDeleted(
            SensorCollectionRegistration arg0) {
        logger.log(Level.INFO, "called");
        return SensorCollectionRegistrationResponseCode.DELETE_OK;
    }

    /**
     * Called when a sensor collection that is already registered has been
     * updated. The Sensor Collection is already updated in the
     * SensorCollectionRegistry. This call is for the platform to act if
     * necessary.
     *
     * @see SensorCollectionRegistry
     * @param registration
     *            - A reference to the entry in SensorCollectionRegistry.
     * @return SensorCollectionRegistrationResponseCode - Indicating
     */
    public SensorCollectionRegistrationResponseCode sensorCollectionRegistrationUpdated(
            SensorCollectionRegistration arg0) {
        logger.log(Level.INFO, "called");
        return SensorCollectionRegistrationResponseCode.ADD_OK;
    }


    /**
     * Called when Sensation distributes a FOTA update package for application
     * update.
     *
     * @param updatePackage
     *            The update package.
     * @return UpdatePackageResponseCode - Indicating result of update process.
     * @see UpdatePackage
     * @see UpdatePackageResponseCode
     */
    public UpdatePackageResponseCode updateApplication(UpdatePackage arg0) {
        logger.log(Level.INFO, "called");
        return UpdatePackageResponseCode.OK;
    }


    /**
     * Called from Sensation when settings for a data collector is updated.
     * @param properties @see {@link SensationClientProperties}
     */
    public void updateDataCollectorSettings(SensationClientProperties arg0) {
        logger.log(Level.INFO, "called");
    }

    /**
     * Called from Sensation requesting an update of the status of the data
     * collector.
     *
     * @return DataCollectorStatus populated with the current status of the data
     *         collector.
     */
    public DataCollectorStatus updateDataCollectorStatus() {
        logger.log(Level.INFO, "called");
        DataCollectorStatus status = new DataCollectorStatus();
        status.setStatus(DataCollectorStatusCode.OK);
        return status;
    }

    /**
     * Called when Sensation distributes a FOTA update package for sensor
     * collection update.
     *
     * @param registration
     *            - The registered sensor collection to update.
     * @param updatePackage
     *            The update package.
     * @return UpdatePackageResponseCode - Indicating result of update process.
     * @see UpdatePackage
     * @see UpdatePackageResponseCode
     */

    public UpdatePackageResponseCode updateSensorCollection(SensorCollectionRegistration arg0, UpdatePackage arg1) {
        logger.log(Level.INFO, "called");
        return UpdatePackageResponseCode.OK;
    }

    /**
     * A request to update the current status of a sensor collection
     * registration.
     *
     * @param regisration
     *            - The registration to update containing registered values.
     *
     * @return The updated SensorCollectionRegistration.
     * @see SensorCollectionRegistration
     */
    public SensorCollectionRegistration updateSensorCollectionStatus(SensorCollectionRegistration arg0) {
        logger.log(Level.INFO, "called");
        return null;
    }

    /**
     * Called when Sensation distributes a FOTA update package for system
     * update.
     *
     * @param updatePackage
     *            The update package.
     * @return UpdatePackageResponseCode - Indicating result of update process.
     * @see UpdatePackage
     * @see UpdatePackageResponseCode
     */
    public UpdatePackageResponseCode updateSystem(UpdatePackage arg0) {
        logger.log(Level.INFO, "called");
        return UpdatePackageResponseCode.OK;
    }

}