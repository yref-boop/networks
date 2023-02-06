package es.udc.redes.tutorial.udp.server;

import java.net.*;

// UDP echo server implementation

public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket sDatagram = null;
        try {
            // Create a server socket
            sDatagram = new DatagramSocket(5000);

            // 300s timeout
            sDatagram.setSoTimeout(300000);

            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) {
                // Prepare datagram for reception
                DatagramPacket dgramRec = new DatagramPacket (receiveData, receiveData.length);
                // Receive the message
                sDatagram.receive (dgramRec);
                System.out.println ("SERVER: recieved "
                    + new String (dgramRec.getData(), 0, dgramRec.getLength())
                    + "from " + dgramRec.getAddress().toString() + ":"
                    + dgramRec.getPort());

                // Prepare datagram to send response
                String sentence = new String (dgramRec.getData());
                InetAddress IPAddress = dgramRec.getAddress();
                int port = dgramRec.getPort();
                sendData = sentence.getBytes();

                // Send response
                DatagramPacket dgramSent = new DatagramPacket (sendData, sendData.length, IPAddress, port);
                sDatagram.send(dgramSent);

                System.out.println ("SERVER: sending "
                    + new String (dgramRec.getData(), 0, dgramRec.getLength())
                    + " to " + dgramRec.getAddress().toString() + ":"
                    + dgramRec.getPort());
            }

        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // close the socket
            try {
                sDatagram.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
