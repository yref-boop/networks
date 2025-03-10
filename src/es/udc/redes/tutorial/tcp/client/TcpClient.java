package es.udc.redes.tutorial.tcp.client;

import java.net.*;
import java.io.*;

/**
 * Implements an echo client using TCP
 */
public class TcpClient {

    public static void main(String argv[]) {
        if (argv.length != 3) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.client.TcpClient <server_address> <port_number> <message>");
            System.exit(-1);
        }
        Socket socket = null;
        try {
            // obtain arguments
            InetAddress serverAddress = InetAddress.getByName(argv[0]);
            int serverPort = Integer.parseInt(argv[1]);
            String message = argv[2];

            // create socket & establish server connection
            socket = new Socket(serverAddress, serverPort);
            socket.setSoTimeout(300000);
            System.out.println("CLIENT: Connection established with "
                    + serverAddress.toString() + " port " + serverPort);

            // set I/O channels
            BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sOutput = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("CLIENT: Sending " + message +
                    " to " + socket.getInetAddress().toString() +
                    ":" + socket.getPort());

            // send message to server
            sOutput.println(message);

            // receive response
            String received = sInput.readLine();
            System.out.println("CLIENT: Received " + received
                    + " from " + socket.getInetAddress().toString()
                    + ":" + socket.getPort());

            // close & release connection
            sOutput.close();
            sInput.close();

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
