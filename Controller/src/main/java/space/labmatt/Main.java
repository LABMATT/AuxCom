package space.labmatt;

import space.labmatt.DeviceMethods.ConnectionTrigger;
import space.labmatt.DeviceMethods.DeviceConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("Controller - LABMATT - Matthew Lewington");

        // Device list is a list of currently connected devices.
        // When a socket connects then add it to the device array.
        List<Thread> devices = new ArrayList<>();
        boolean running = true;

        // Server socket is created per Controller program.
        // Once connected then threads with socket can be created on this platform.
        ServerSocket serverSocket = null;
        String serverSocketException = "";
        try {

            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {

            serverSocketException = e.getMessage();
        }

        if (serverSocket == null) {

            // If the server socket is not bound then quit the program and print an error message.
            System.out.println("Failed to create socket. Null serversocket: " + serverSocketException);
            System.exit(0);
        }

        if (!serverSocket.isBound()) {

            // If the server socket is not bound then quit the program and print an error message.
            System.out.println("Failed to bind to server socket: " + serverSocketException);
            System.exit(0);
        }

        ConnectionTrigger connectionTrigger = new ConnectionTrigger();
        while (running) {

            // If there are no curly active connections then open another thread for port.
            if (!connectionTrigger.isActiveConnection()) {

                connectionTrigger.setActiveConnection(true);
                DeviceConnection deviceConnection = new DeviceConnection(serverSocket, connectionTrigger);
                Thread thread = new Thread(deviceConnection);
                thread.start();
            }
        }

        System.out.println("List: " + Arrays.toString(devices.toArray()));
    }
}