package space.labmatt.DeviceMethods;

import java.util.List;

public class DecodeInfo {

    /**
     * Decodes info proved by controller. If info is unintellajable then throw and error.
     * IF decoding is sucessfull then return a device.
     *
     * First 4 into are device type.
     * Second 10 bytes are the device ID.
     *
     * @param deviceInfoBytes List of bytes recived once reqested by the controller.
     * @return
     * @throws Exception If bytes are not decidable then throw an exception.
     */
    public Device decodeIno(List<Integer> deviceInfoBytes) throws Exception{
        Device device = new Device();

        //throw new Exception("Malformed Header info.");
        return device;
    }
}
