package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) { this.socket = socket; }

    public void run() {
        try {
            BufferedReader reader  = new BufferedReader (new InputStreamReader (socket.getInputStream()));
            String message = reader.readLine();
            System.out.println ("SERVER: Received " + message + " from "
                                + socket.getInetAddress ().toString () + ":"
                                + socket.getPort ());

            if (message == null) return;

            String[] message_parts = message.split (" ");
            File file = new File ("p1-files" + message_parts[1]);

            if (!file.exists()) {

                file = new File ("p1-files/error404.html");
                FileInputStream input = new FileInputStream (file.toString());
                OutputStream client_output = socket.getOutputStream();
                client_output.write (("HTTP/1.0 404 Not Found\r\n").getBytes());

                write_contents (file, client_output);

                if (!message_parts[0].equals ("HEAD")) {
                    int c;
                    while ((c = input.read()) != -1)
                        client_output.write(c);
                }
                client_output.flush();
                client_output.close();
                return;
            }

            if (message_parts[0].equals ("GET")) {

                FileInputStream input = new FileInputStream (file.toString());
                OutputStream client_output = socket.getOutputStream();
                boolean isModifiedSince = true;
                Date modifiedSince;
                DateFormat sdf = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);
                String line = reader.readLine();
                while(!line.equals("")) {
                    String[] line_parts = line.split(": ");
                    if (line_parts[0].equals ("If-Modified-Since")){
                        Date fechaUltimaMod = new Date (file.lastModified());
                        modifiedSince = sdf.parse (line_parts[1]);
                        if (!fechaUltimaMod.before (modifiedSince)) {
                            isModifiedSince = false;
                            break;
                        }
                    }
                    line = reader.readLine();
                }
                if (!isModifiedSince)
                    client_output.write (("HTTP/1.0 304 Not Modified\r\n").getBytes());
                else
                    client_output.write (("HTTP/1.0 200 OK\r\n").getBytes());

                write_contents (file, client_output);

                if (isModifiedSince) {
                    int c;
                    while ((c = input.read()) != -1)
                        client_output.write(c);
                }
                    client_output.flush();
                    client_output.close();
                return;
            }

            if (message_parts[0].equals ("HEAD")) {

                OutputStream client_output = socket.getOutputStream();
                boolean isModifiedSince = true;Date modifiedSince;
                DateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);
                String line = reader.readLine();
                while(!line.equals("")) {
                    String[] line_parts = line.split(": ");
                    if (line_parts[0].equals("If-Modified-Since")){
                        Date fechaUltimaMod = new Date(file.lastModified());
                        modifiedSince = sdf.parse(line_parts[1]);
                        if (!fechaUltimaMod.before(modifiedSince)) {
                            isModifiedSince = false;
                            break;
                        }
                    }
                    line = reader.readLine();
                }
                if (!isModifiedSince)
                    client_output.write(("HTTP/1.0 304 Not Modified\r\n").getBytes());
                else
                    client_output.write(("HTTP/1.0 200 OK\r\n").getBytes());

                write_contents (file, client_output);

                client_output.flush();
                client_output.close();
                return;
            }

            file = new File ("p1-files/error404.html");
            FileInputStream input_stream = new FileInputStream (file.toString());
            OutputStream client_output = socket.getOutputStream();

            client_output.write (("HTTP/1.0 400 Bad Request\r\n").getBytes());

            write_contents (file, client_output);

            int c;
            while ((c = input_stream.read()) != -1)
                client_output.write(c);
            client_output.flush();
            client_output.close();

        } catch (SocketTimeoutException exception) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception exception) {
            System.err.println("Error: " + exception.getMessage());
        }
        finally {
            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void content_type(File file,OutputStream output) throws IOException {

        String[] parts = file.toString().split("\\.");
        System.out.println(parts[1]);
        switch (parts[1]) {
            case "html" : output.write(("Content-Type: text/html\r\n").getBytes());
                break;
            case "txt" : output.write(("Content-Type: text/plain\r\n").getBytes());
                break;
            case "gif" : output.write(("Content-Type: image/gif\r\n").getBytes());
                break;
            case "png" : output.write(("Content-Type: image/png\r\n").getBytes());
                break;
            default : output.write(("Content-Type: application/octet-stream\r\n").getBytes());
        }
    }

    private void write_contents (File file, OutputStream output) throws IOException {

        DateFormat date_format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);
        String date = date_format.format(new Date());
        String modification_date = date_format.format(file.lastModified());

        output.write (("Date: " + date + "\r\n").getBytes());
        output.write (("Server: web_server\r\n").getBytes());
        output.write (("Content-Length: " + file.length()+"\r\n").getBytes());
        content_type (file, output);
        output.write (("Last-Modified:" + modification_date +"\r\n").getBytes());
        output.write (("\r\n").getBytes());
    }
}

