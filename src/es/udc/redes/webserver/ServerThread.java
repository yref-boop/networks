package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ServerThread extends Thread {

    BufferedReader input;
    DataOutputStream output;
    PrintWriter printer;
    File file;
    Date date;
    long modified;

    private Socket socket;

    public ServerThread(Socket socket) {this.socket = socket;}

    public void run() {
        try {
            input  = new BufferedReader (new InputStreamReader (socket.getInputStream()));
            output = new DataOutputStream (socket.getOutputStream());
            printer = new PrintWriter (socket.getOutputStream(), true);

            String line = input.readLine();
            StringTokenizer token = new StringTokenizer (line);
            String method = token.nextToken();
            String file_name   = token.nextToken();
            file_name = ("p1-files" + file_name);

            FileInputStream input_stream = null;
            boolean found = true;

            try {
                input_stream = new FileInputStream (file_name);
            } catch (FileNotFoundException e) {
                found = false;
            }

            date = new Date ();
            file = new File (file_name);
            modified = file.lastModified();
            SimpleDateFormat date_format = new SimpleDateFormat ("MMMM dd, yyyy, hh:mm a");
            String server = "/nServer: WebServer";

            String STATUS_LINE, HEADER_LINE;
            String BODY = null;
            if (!found) {
                STATUS_LINE = "HTTP/1.0 404 NOT FOUND";
                HEADER_LINE = "Date: " + date;
                BODY =  "<html>\n\t<head>\n\t\t<title>\n\t\t\tERROR 404\n\t\t</title>\n\t</head>\n\t<body>\n\t\t<p>ERROR 404</p>\n\t</boddy>\n</html>";
            } else {
                STATUS_LINE = "HTTP/1.0 200 OK";
                HEADER_LINE = "Date: " + date + server + "\nContentLength: " + file.length() + "\nContentType: " + contentType(file_name) + "\nLast-Modified: " + date_format.format(modified);
                HEADER_LINE = "Date: " + date;
            }
            printer.println (STATUS_LINE);
            printer.println (HEADER_LINE);
            if ((!found) && (BODY != null))
                printer.println (BODY);
            else {
                printer.println ("");
                int n;
                if ((method != "HEAD") && (input_stream != null)){
                    n = input_stream.read ();
                    while (n != -1)
                        output.write(n);
                }
                input_stream.close ();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try{ socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private String contentType (String file) {
        if (file.endsWith (".html"))
            return ("text/html");
        else if (file.endsWith (".txt"))
            return ("text/plain");
        else if (file.endsWith (".gif"))
            return ("image/gif");
        else if (file.endsWith (".png"))
            return ("image/png");
        else
            return ("application/octet-stream");
    }
}
