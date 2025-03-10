package es.udc.redes.tutorial.copy;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) {

        FileInputStream input   = null;
        FileOutputStream output = null;

        try {
            // initialize streams
            input  = new FileInputStream(args[0]);
            output = new FileOutputStream(args[1]);

            // copy until eof
            int n; while ((n = input.read()) != -1) output.write(n);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        finally {
            try {
                // close streams
                if (input != null) input.close();
                if (output != null) output.close();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
