package es.udc.redes.tutorial.udp.client;

import java.net.*;

/**
 * Implements an echo client using UDP
 */
public class UdpClient {

    public static void main(String argv[]) {
        if (argv.length != 3) {
            System.err.println("Format: es.udc.redes.tutorial.udp.client.UdpClient <server_address> <port_number> <message>");
            System.exit(-1);
        }
        DatagramSocket sDatagram = null;
        try {
            // create a non connection-oriented socket (use any available port)
            sDatagram = new DatagramSocket();

            sDatagram.setSoTimeout(300000);

            // obtain arguments
            InetAddress serverAddress = InetAddress.getByName(argv[0]);
            int serverPort = Integer.parseInt(argv[1]);
            String message = argv[2];

            // prepare datagram
            DatagramPacket dgramSent = new DatagramPacket(message.getBytes(),
                    message.getBytes().length, serverAddress, serverPort);
            // send datagram
            sDatagram.send(dgramSent);
            System.out.println("CLIENT: Sending "
                    + new String(dgramSent.getData()) + " to "
                    + dgramSent.getAddress().toString() + ":"
                    + dgramSent.getPort());
            // prepare datagram for data reception
            byte array[] = new byte[1024];
            DatagramPacket dgramRec = new DatagramPacket(array, array.length);
            // receive message
            sDatagram.receive(dgramRec);
            System.out.println("CLIENT: Received "
                    + new String(dgramRec.getData(), 0, dgramRec.getLength())
                    + " from " + dgramRec.getAddress().toString() + ":"
                    + dgramRec.getPort());
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // close socket
            sDatagram.close();
        }
    }
}
