package space.labmatt.DeviceMethods;

import java.util.ArrayList;
import java.util.List;

public class Device {

    public List<String> runninglog = new ArrayList<>();
    public int deviceType = 0;
    public String deviceTypeString = "";
    public int softwareVersion = 0;
    public int deviceID = 0;

    public String deviceName = "";

    // Device permeates that are common across multiple settings.
    boolean deviceState = false;
    int currentReading  = 0;
    int voltageReading  = 0;
    long tempReading    = 0;



    /**
     * Using the device ID then fill the string type in to make it easy to understand.
     */
    public void deviceTypeInterprate() {

        switch (deviceType) {
            case 1:
                deviceTypeString = "relay";
                break;
            case 2:
                deviceTypeString = "input";
                break;
            case 3:
                deviceTypeString = "magneticSensor";
                break;
            case 4:
                deviceTypeString = "temp";
                break;
            case 5:
                deviceTypeString = "thermocouple";
                break;
        }
    }
}
