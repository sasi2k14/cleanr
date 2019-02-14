package sasi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Tests if file is moved to another location and that
 * its last access time has been changed.
 */
public class LastAccessTimeOSTest {
    public static void main(String[] args) throws IOException {

        String src  = "/Users/sasikumar/Desktop/107658M_s01_PPS3_Door Hangers Craft_FAOL_1.pdf";
        String dest = "/Users/sasikumar/Downloads/107658M_s01_PPS3_Door Hangers Craft_FAOL_1.pdf";

        BasicFileAttributes attributes =
                Files.getFileAttributeView(Paths.get(src), BasicFileAttributeView.class).readAttributes();

        System.out.println("Before move: " + attributes.creationTime());

        Files.move(Paths.get(src), Paths.get(dest));

        attributes = Files.getFileAttributeView(Paths.get(dest), BasicFileAttributeView.class).readAttributes();

        System.out.println("After move: " + attributes.creationTime());


    }
}
