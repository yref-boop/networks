package es.udc.redes.tutorial.info;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;

public class Info {

    public static void main(String[] args) {
        try {
            // read path
            Path path = Paths.get(args[0]);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            // print obtained attributes
            System.out.println("size: " + attributes.size());
            System.out.println("last modified time: " + attributes.lastModifiedTime());
            String filename = path.getFileName().toString();
            if (filename.contains(".")) {
                String[] tokens = filename.split("\\.");
                System.out.println("name: " + tokens[0]);
                System.out.println("extension: " + tokens[1]);
            }
            else {
                System.out.println("name: " + filename);
                System.out.println("extension not applicable");
            }
            if (attributes.isRegularFile())
                System.out.println("filetype: " + Files.probeContentType(path));
            if (attributes.isDirectory())
                System.out.println("filetype: directory");
            if (attributes.isSymbolicLink())
                System.out.println("filetype: symbolic-link");
            if (attributes.isOther())
                System.out.println("filetype: other");
            System.out.println("absolute path: " + path.toAbsolutePath().toString());
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
