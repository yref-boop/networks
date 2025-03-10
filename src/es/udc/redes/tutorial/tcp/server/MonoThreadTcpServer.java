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

        ServerSocket server_socket = null;
        Socket client_socket = null;

        try {

            // create server socket
            int port = Integer.parseInt(argv[0]);
            server_socket = new ServerSocket(port);
            server_socket.setSoTimeout(300000);

            while (true) {
                // wait for connections
                client_socket = server_socket.accept();

                // set I/O channels
                InputStream input = client_socket.getInputStream();
                OutputStream output = client_socket.getOutputStream();

                // receive client message
                BufferedReader reader = new BufferedReader (new InputStreamReader (input));
                String line = reader.readLine();
                System.out.println("SERVER: Received " + line
                    + " from " + client_socket.getInetAddress().toString()
                    + ":" + client_socket.getPort());

                // send response
                PrintWriter writer = new PrintWriter (output, true);
                writer.println(line);
                System.out.println("SERVER: Sending " + line +
                " to " + client_socket.getInetAddress().toString() +
                ":" + client_socket.getPort());

                // close the streams
                input.close();
                output.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
          } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // close the socket
            try {
                if (server_socket != null) {
                    server_socket.close();
                }
                if (client_socket != null){
                    client_socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
