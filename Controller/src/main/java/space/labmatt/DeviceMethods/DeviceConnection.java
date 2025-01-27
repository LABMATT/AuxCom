package space.labmatt.DeviceMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DeviceConnection implements Runnable {

    public String deviceName = "";
    public int deviceID = 0;
    public Socket deviceSocket = null;
    public ServerSocket serverSocket = null;
    private ConnectionTrigger connectionTrigger;

    public DeviceConnection(ServerSocket serverSocket, ConnectionTrigger connectionTrigger) {

        this.serverSocket = serverSocket;
        this.connectionTrigger = connectionTrigger;
    }

    @Override
    public void run() {
        System.out.println("Awaiting connection.");

        try {

            Socket socket = serverSocket.accept();
            System.out.println("Socket Connection: " + socket);
            connectionTrigger.setActiveConnection(false);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            socket.getOutputStream().write('y');

            String bytein = "";
            boolean run = true;
            while ((bytein = bufferedReader.readLine()) != null) {

                System.out.println(bytein);

            }
            socket.getOutputStream().write('r');

        } catch (Exception e) {

            System.out.println("Exception" + e.getMessage());
        }

        connectionTrigger.setActiveConnection(false);
    }
}
