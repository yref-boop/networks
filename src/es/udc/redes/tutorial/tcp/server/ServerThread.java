package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {

        BufferedReader bReader = null;
        PrintWriter pWriter = null;

        try {
            // set reader & writter
            bReader = new BufferedReader (new InputStreamReader (socket.getInputStream()));
            pWriter = new PrintWriter (socket.getOutputStream(), true);

            // recieve client's message
            String message = bReader.readLine();
            System.out.println ("SERVER: Received " + message
                + " from " + socket.getInetAddress().toString()
                + ":" + socket.getPort());

            // answer message
            pWriter.println(message);
            System.out.println ("SERVER: Sending " + message
                + " to " + socket.getInetAddress().toString() +
                ":" + socket.getPort());

            // close streams
            if (bReader != null) bReader.close();
            if (pWriter != null) pWriter.close();

        }
        catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            // close the socket
            try {
                if (socket != null) socket.close();
            }
            catch (IOException e) {
                throw new RuntimeException (e);
            }
        }
    }
}
