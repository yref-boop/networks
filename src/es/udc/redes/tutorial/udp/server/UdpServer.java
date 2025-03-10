package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }

        DatagramSocket socket = null;

        try {

            // create a server socket
            socket = new DatagramSocket(5000);

            // set timeout and data
            socket.setSoTimeout(300000);
            byte[] in_data = new byte[1024];
            byte[] out_data;

            while (true) {
                // prepare datagram for reception
                DatagramPacket in_datagram = new DatagramPacket(in_data, in_data.length);

                // receive the message
                socket.receive(in_datagram);
                System.out.println ("SERVER: recieved "
                    + new String (in_datagram.getData(), 0, in_datagram.getLength())
                    + " from " + in_datagram.getAddress().toString() + ":"
                    + in_datagram.getPort());
                // prepare datagram to send response
                String sentence = new String (in_datagram.getData());
                InetAddress address = in_datagram.getAddress();
                int port = in_datagram.getPort();
                out_data = sentence.getBytes();

                // send response
                DatagramPacket out_datagram = new DatagramPacket (out_data, out_data.length, address, port);
                socket.send(out_datagram);

                System.out.println ("SERVER: sending "
                    + new String (out_datagram.getData(), 0, out_datagram.getLength())
                    + " to " + out_datagram.getAddress().toString() + ":"
                    + out_datagram.getPort());
            }

        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // close socket
            try {
                socket.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
