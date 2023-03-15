package es.udc.redes.webserver;

import java.io.*;
import java.net.*;

public class WebServer {

    public static void main (String[] args) {
        if (args.length != 1) {
            System.err.println ("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit (-1);
        }

        int port = Integer.parseInt (args[0]);
        ServerSocket server_socket = null;

        try {
            server_socket = new ServerSocket (port);
            server_socket.setSoTimeout (300000);

            while (true) {
 
                Socket socket = server_socket.accept();
                ServerThread server_thread = new ServerThread (socket);
                server_thread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println ("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println ("Error: " + e.getMessage());
            e.printStackTrace ();
        } finally {
            try {
                if (server_socket != null)
                    server_socket.close();
            } catch (IOException e) {
                throw new RuntimeException (e);
            }
        }
    }
}
