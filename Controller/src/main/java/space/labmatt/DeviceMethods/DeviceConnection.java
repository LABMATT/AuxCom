package space.labmatt.DeviceMethods;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceConnection implements Runnable {

    public ServerSocket serverSocket = null;
    private ConnectionTrigger connectionTrigger;
    public Device device = null;

    public DeviceConnection(ServerSocket serverSocket, ConnectionTrigger connectionTrigger) {

        this.serverSocket = serverSocket;
        this.connectionTrigger = connectionTrigger;
    }

    @Override
    public void run() {

        String threadName = Thread.currentThread().getName() + ": ";
        System.out.println(threadName + "Awaiting connection.");
        Socket socket = null;

        try {

            socket = serverSocket.accept();
            System.out.println(threadName + "Socket Connection: " + socket);
            connectionTrigger.setActiveConnection(false);

            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Ask device for its info.
            socket.getOutputStream().write('I'); // Send I or ascii 49. Asking the device for its info.

            // Wait for a byte to arrive. If nothing arrives within
            long startMillies = System.currentTimeMillis();
            long timePassed = 0;
            boolean messageRecived = false;
            List<Integer> recivedInfoBytes = new ArrayList<>();

            // Read the socket for bytes until either 4s has passed or we receive the termination charter of ;.
            while (timePassed < 4000 && !messageRecived) {

                timePassed = Math.subtractExact(System.currentTimeMillis(), startMillies);

                if (socket.getInputStream().available() > 0) {

                    int streamByte = socket.getInputStream().read();
                    recivedInfoBytes.add(streamByte);
                }

                if (!recivedInfoBytes.isEmpty() && recivedInfoBytes.getLast() == 59) {
                    messageRecived = true;
                }
            }


            // If the reading is timed out then produce this message.
            if (!messageRecived) {

                socket.close();
                throw new Exception("Timeout getting device info. Device: " + socket.getInetAddress());

            } else {

                System.out.println(threadName + "Device Info received.");
                System.out.println(threadName + "info: " + Arrays.toString(recivedInfoBytes.toArray()));
            }

            // Using bytes decode info. If the header is malformed then throw and exception.
            DecodeInfo decodeInfo = new DecodeInfo();
            device = decodeInfo.decodeIno(recivedInfoBytes);

            switch (device.deviceTypeString) {
                case "relay":
                    break;
                case "input":
                    break;
            }


            // Finsih connection.
            socket.close();

        } catch (Exception e) {

            System.out.println(threadName + "Exception: " + e);
        }

        System.out.println(threadName + "Thread Halted.");
    }
}
