package sasi;

import java.nio.file.Path;

/**
 * Created by sasikumar on 8/13/16.
 */
public class Configuration {

    private Path scanPath;
    private int maxArchivalDays;
    private int maxTrashDays;

    public int getMaxTrashDays() {
        return maxTrashDays;
    }

    public void setMaxTrashDays(int maxTrashDays) {
        this.maxTrashDays = maxTrashDays;
    }

    public int getMaxArchivalDays() {
        return maxArchivalDays;
    }

    public void setMaxArchivalDays(int maxArchivalDays) {
        this.maxArchivalDays = maxArchivalDays;
    }

    public Path getScanPath() {
        return scanPath;
    }

    public void setScanPath(Path scanPath) {
        this.scanPath = scanPath;
    }
}
