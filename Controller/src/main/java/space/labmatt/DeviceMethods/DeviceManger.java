package space.labmatt.DeviceMethods;

import java.util.ArrayList;
import java.util.List;

public class DeviceManger {

    public static List<Device> devices = new ArrayList<>();

    /**
     * Returns a list of the devices that have been added by the device connection sockets.
     * @return
     */
    public synchronized List<Device> getDeviceList() {

        return devices;
    }
}
