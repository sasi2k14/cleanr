package sasi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by sasikumar on 8/13/16.
 */
public class Cleanr {

    Configuration config;

    public Cleanr(Configuration config) {
        this.config = config;
    }

    public List<File> scanFilesForArchival() {
        try {
            Stream<Path> allFiles = Files.list(config.getScanPath());
            

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return null;
    }
}
