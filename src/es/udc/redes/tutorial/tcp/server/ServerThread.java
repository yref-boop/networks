package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
    // Store the socket s
        this.socket = s;
  }

  @Override
  public void run() {

      BufferedReader bReader = null;
      PrintWriter pWriter = null;

    try {
      // Set the input channel
      bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // Set the output channel 
      pWriter = new PrintWriter(socket.getOutputStream(), true);
      // Receive the message from the client
      String message = bReader.readLine();
       System.out.println("SERVER: Received " +
 message
                    + " from " + socket.getInetAddress().toString()
                    + ":" + socket.getPort());

      // Sent the echo message to the client
      pWriter.println(message);
       System.out.println("SERVER: Sending " + message +
                    " to " + socket.getInetAddress().toString() +
                    ":" + socket.getPort());
      // Close the streams
        if (bReader != null) {
            bReader.close();
        }

        if (pWriter != null) {
            pWriter.close();
        }

    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
	// Close the socket
        try {
        if (socket != null) {
            socket.close();
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
      }
  }
}
