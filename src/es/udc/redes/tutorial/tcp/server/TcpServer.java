package es.udc.redes.tutorial.tcp.server;
import java.io.*;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }

        int port = Integer.parseInt(argv[0]);
        ServerSocket serverSocket = null;

        try {
            // set socket
            serverSocket = new ServerSocket (port);
            serverSocket.setSoTimeout(300000);

            while (true) {
                Socket socket = serverSocket.accept(); // wait connections
                ServerThread serverThread = new ServerThread (socket); // create threads 
                serverThread.start(); // iniate thread
            }
        }
        catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            // close the socket
            try {
                if (serverSocket != null) serverSocket.close();
            }
            catch (IOException e) {
                throw new RuntimeException (e);
            }
        }
    }
}
