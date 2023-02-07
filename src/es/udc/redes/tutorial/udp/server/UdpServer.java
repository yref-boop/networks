package es.udc.redes.tutorial.udp.server;

import java.net.*;

// UDP echo server implementation

public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket datagram_socket = null;
        try {
            // Create a server socket
            datagram_socket = new DatagramSocket(5000);

            // 300s timeout
            datagram_socket.setSoTimeout(300000);

            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) {
                // Prepare datagram for reception
                DatagramPacket datagram_in = new DatagramPacket (receiveData, receiveData.length);
                // Receive the message
                datagram_socket.receive (datagram_in);
                System.out.println ("SERVER: recieved "
                    + new String (datagram_in.getData(), 0, datagram_in.getLength())
                    + "from " + datagram_in.getAddress().toString() + ":"
                    + datagram_in.getPort());

                // Prepare datagram to send response
                String sentence = new String (datagram_in.getData());
                InetAddress IPAddress = datagram_in.getAddress();
                int port = datagram_in.getPort();
                sendData = sentence.getBytes();

                // Send response
                DatagramPacket datagram_out = new DatagramPacket (sendData, sendData.length, IPAddress, port);
                datagram_socket.send(datagram_out);

                System.out.println ("SERVER: sending "
                    + new String (datagram_in.getData(), 0, datagram_in.getLength())
                    + " to " + datagram_in.getAddress().toString() + ":"
                    + datagram_in.getPort());
            }

        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // close the socket
            try {
                datagram_socket.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
