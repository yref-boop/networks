package es.udc.redes.webserver;
import java.text.*;
import java.util.Calendar;
import java.util.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {

    BufferedReader input;
    DataOutputStream output;
    PrintWriter printer;

    File fl;
    Date date;
    long smodified;

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            printer = new PrintWriter(socket.getOutputStream(), true);

            String line = input.readLine();
            StringTokenizer stokenized = new StringTokenizer(line);
            String method = stokenized.nextToken();
            String file = stokenized.nextToken();
            file = ("p1-files" + file);

            FileInputStream instr = null;
            boolean found = true;
            try {
                instr = new FileInputStream(file);
            } catch (FileNotFoundException e){
                found = false;
            }
            date = new Date();
            fl = new File(file);
            smodified = fl.lastModified();
            SimpleDateFormat dformat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
            String server = "/nServer: WebServer_767";

            String STATUS_LINE, HEADER_LINE;
            String BODY = null;
            if (!(found)){
                STATUS_LINE = "HTTP/1.0 404 NOT FOUND";
                HEADER_LINE = "Date: " + date;
                BODY =  "<html>\n\t<head>\n\t\t<title>\n\t\t\tERROR 404\n\t\t</title>\n\t</head>\n\t<body>\n\t\t<p>ERROR 404</p>\n\t</boddy>\n</html>";
            } else {
                STATUS_LINE = "HTTP/1.0 200 OK";
                HEADER_LINE = "Date: " + date + server + "\nContentLength: " + fl.length() + "\nContentType: " + contentType(file) + "\nLast-Modified: " + dformat.format(smodified);
            }
            printer.println(STATUS_LINE);
            printer.println(HEADER_LINE);
            if(!(found)&&(BODY != null))
                printer.println(BODY);
            else {
                printer.println("");
                int n;
                if((method != "HEAD") && (null != instr)){
                    n = instr.read();
                    while(n != -1){
                        output.write(n);
                }
                instr.close();
                }
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
    private String contentType(String file){
        if(file.endsWith(".html")){
            return "text/html";
        }
        if(file.endsWith(".txt")){
            return "text/plain";
        }
        if(file.endsWith(".gif")){
            return "image/gif";
        }
        if(file.endsWith(".png")){
            return "image/png";
        }
        return "application/octet-stream";
    }
}
