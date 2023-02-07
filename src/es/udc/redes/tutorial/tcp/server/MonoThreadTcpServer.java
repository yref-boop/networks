package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        
        int port = Integer.parseInt(argv[0]);
        ServerSocket serverSocket = null;

        try {
            // create socket
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(300000);
            
            while (true) {

                // initial values
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                // recieve message
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();
                System.out.println("SERVER: Received " + line
                    + " from " + socket.getInetAddress().toString()
                    + ":" + socket.getPort());

                // answer
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(line);
                System.out.println("SERVER: Sending " + line +
                    " to " + socket.getInetAddress().toString() +
                    ":" + socket.getPort());

                socket.close();
            }
        }
        catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
