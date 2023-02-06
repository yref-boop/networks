package es.udc.redes.webserver;

import java.io.*;
import java.net.*;

public class WebServer {

  public static void main(String argv[]) {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
      System.exit(-1);
    }

    int port = Integer.parseInt(argv[0]);
    ServerSocket serverSocket = null;

    try {
      // Create a server socket
      serverSocket = new ServerSocket(port);
      // Set a timeout of 300 secs
      serverSocket.setSoTimeout(300000);

      while (true) {
        // Wait for connections
        Socket socket = serverSocket.accept();
        // Create a ServerThread object, with the new connection as parameter
        ServerThread serverThread = new ServerThread(socket);
        // Initiate thread using the start() method
        serverThread.start();
      }
    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
          System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
	    //Close the socket
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
  }
}
