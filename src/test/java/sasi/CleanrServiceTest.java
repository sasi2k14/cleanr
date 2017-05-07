package sasi;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by qgb368 on 8/12/2016.
 */
public class CleanrServiceTest {

    /**
     * Creates a set of temp files under a temp directory
     * @param numberOffiles
     * @return Path of temp Directory
     * @throws IOException
     */
    private Path createTempFiles(int numberOffiles) throws IOException {
        Path tempDir = Files.createTempDirectory("cleanr-app-test");

        for(int i=0 ; i<numberOffiles; i++){
            Path tempFile = Files.createTempFile(tempDir, "cleanrservicetest", "");
            System.out.printf("Created temp file:%s\n", tempFile);
        }

        return tempDir;
    }

    private void updateFilesLastAccessed30DaysBack(List<Path> files) throws IOException {
        for(Path file : files) {
            int daysOld = ThreadLocalRandom.current().nextInt(31, 9999);
            FileTime lastAccessedTime = FileTime.from(Instant.now().plus(daysOld, ChronoUnit.DAYS));
            Files.setAttribute(file, "lastAccessTime", lastAccessedTime);

            System.out.println( Files.getAttribute(file, "lastAccessTime"));
        }
    }

    @Test public void testCleanrFindilesOlderThan30daysForArchival() throws IOException {
        // Create a temp file on a temp folder
        Path tempFilesDir = createTempFiles(1);
        Assert.assertTrue( Files.list(tempFilesDir).count() == 1);

        // Update the modified timestamp to be more than 30days
        List allFiles = Files.list(tempFilesDir).collect(Collectors.toList());
        updateFilesLastAccessed30DaysBack(allFiles);

        // Run archival on the temp folder
        // Update configuration to point the temp file path
//        Configuration config = new Configuration();
//        config.setScanPath(tempFilesDir);
        Cleanr cleanr = new Cleanr(tempFilesDir);

        List<Path> files = cleanr.scanFilesForArchival();

        Assert.assertTrue( files.size() == 1);
    }

    @Test public void testArchiveFilesOn1stSweep() throws IOException {
        Path path = Paths.get("/Users/sasikumar/Downloads/groovy-2.4.7");
        if ( Files.exists(path) ) {

            System.out.println( Files.getAttribute(path, "lastAccessTime") );

        } else {
            throw new FileNotFoundException();
        }
    }

    @Test public void testDeleteFilesOn2ndSweep() {

    }
}
