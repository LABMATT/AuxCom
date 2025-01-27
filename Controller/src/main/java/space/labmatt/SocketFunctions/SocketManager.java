package space.labmatt.SocketFunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {

    /**
     * Server
     */
    public Socket socketConnection() {

        Socket socket = null;

        try {

            //socket = serverSocket.accept();

            System.out.println("Socket Connection: " + socket);

            /*
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String bytein = "";
            boolean run = true;
            while ((bytein = bufferedReader.readLine()) != null) {


            }
            */

        } catch(Exception e) {

            System.out.println("Exception" + e);
        }

        return socket;
    }
}
