package es.udc.redes.tutorial.copy;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) {

        FileInputStream in  = null;
        FileOutputStream out = null;

        try {
            in  = new FileInputStream(args[0]);
            out = new FileOutputStream(args[1]);

            int n;
            while ((n = in.read()) != -1) out.write(n);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
