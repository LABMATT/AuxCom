package space.labmatt.DeviceMethods;

import java.util.HashMap;

public class DeviceManger {

    HashMap<Integer, Device> activeDevices = new HashMap<>();
    HashMap<Integer, Device> deviceList = new HashMap<>();

    /**
     * Returns a list of the devices that have been added by the device connection sockets.
     *
     * @return
     */
    public synchronized HashMap<Integer, Device> getActiveDevices() {

        return activeDevices;
    }

    /**
     * Gets the device that matches that deviceID
     *
     * @return returns the device
     */
    public synchronized Device getDevice(int deviceIdentifcation) {

        return activeDevices.get(deviceIdentifcation);
    }


    /**
     * Adds the device to the running device list.
     * If the device has already been added in the past, then add it to the active device list.
     * @param device device can be added to the device list if not already in it. Other wisde is used to mark the device as online
     */
    public synchronized addDevice(Device device) {

        if (deviceList.containsKey(device.deviceID)) {

        }
    }
}
