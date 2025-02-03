package space.labmatt.DeviceMethods;

import java.util.List;

public class DecodeInfo {

    /**
     * Decodes info proved by controller. If info is unintellajable then throw and error.
     * IF decoding is sucessfull then return a device.
     * <p>
     * First 4 ints are device type.
     * Second 4 Software type
     * Third 7 bytes are the device ID.
     * <p>
     * FEX: BREAKDOWN:    device type    SoftVersion    DeviceID                Terminator
     * FEX: RAW ASCII:    48 48 48 57    48 48 50 51    00 00 00 00 53 52 56    59
     * FEX: INT ASCII:    0  0  0  9     0  0  2  3      0  0  0  0  5  4  8     ;
     *
     * @param deviceInfoBytes List of bytes recived once reqested by the controller.
     * @return
     * @throws Exception If bytes are not decidable then throw an exception.
     */
    public Device decodeIno(List<Integer> deviceInfoBytes) throws Exception {
        Device device = new Device();

        // If there are not enough bytes for this to be a header than throw an exception.
        if (deviceInfoBytes.size() < 14)
        {
            throw new Exception("Malformed Header. Not enough bytes");
        }

        int digit1 = assciToInt(deviceInfoBytes.get(0)) * 1000;
        int digit2 = assciToInt(deviceInfoBytes.get(1)) * 100;
        int digit3 = assciToInt(deviceInfoBytes.get(2)) * 10;
        int digit4 = assciToInt(deviceInfoBytes.get(3));
        device.deviceType = digit1 + digit2 + digit3 + digit4;

        int digit5 = assciToInt(deviceInfoBytes.get(4)) * 1000;
        int digit6 = assciToInt(deviceInfoBytes.get(5)) * 100;
        int digit7 = assciToInt(deviceInfoBytes.get(6)) * 10;
        int digit8 = assciToInt(deviceInfoBytes.get(7));
        device.softwareVersion = digit5 + digit6 + digit7 + digit8;

        int digit9 = assciToInt(deviceInfoBytes.get(8))   * 1000000;
        int digit10 = assciToInt(deviceInfoBytes.get(9))  * 100000;
        int digit11 = assciToInt(deviceInfoBytes.get(10)) * 10000;
        int digit12 = assciToInt(deviceInfoBytes.get(11)) * 1000;
        int digit13 = assciToInt(deviceInfoBytes.get(12)) * 100;
        int digit14 = assciToInt(deviceInfoBytes.get(13)) * 10;
        int digit15 = assciToInt(deviceInfoBytes.get(14));
        device.deviceID = digit9 + digit10 + digit11 + digit12 + digit13 + digit14 + digit15;

        int digit16 = deviceInfoBytes.get(15);
        if (digit16 != 59)
        {
            throw new Exception("Malformed Header. Terminating charter incorrect.");
        }

        device.deviceTypeInterprate();

        System.out.println("Device type: " + device.deviceType);
        System.out.println("Device soft: " + device.softwareVersion);
        System.out.println("Device ID  : " + device.deviceID + " AKA " + device.deviceTypeString);

        return device;
    }


    /**
     * Converts ascii dec to int.
     *
     * @param ascii The decimal ascii number provided. INT
     * @return Returns the int equivlant to that ascii value. INT
     * @throws Exception If the value cannnot be converted to an int as its not a number then throw and error.
     */
    private int assciToInt(int ascii) throws Exception {

        return switch (ascii) {
            case 48 -> 0;
            case 49 -> 1;
            case 50 -> 2;
            case 51 -> 3;
            case 52 -> 4;
            case 53 -> 5;
            case 54 -> 6;
            case 55 -> 7;
            case 56 -> 8;
            case 57 -> 9;
            default -> throw new Exception("Malformed Header. Unable to convert ascii to int.");
        };
    }
}
