package sasi.filters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class FileCreateTimeBasedFilter implements CleanFilter {

    private int maxExpiryDays = 30;

    public FileCreateTimeBasedFilter() {}

    public FileCreateTimeBasedFilter(int _maxExpiryDays) {
        maxExpiryDays = _maxExpiryDays;
    }

    @Override
    public boolean isPathCanBeCleaned(Path path) {
        if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
            return isFileCreatedTimeBefore(maxExpiryDays, path);
        } else {
            return isDirectoryCanBeDeleted(path);
        }
    }

    private boolean isDirectoryCanBeDeleted(Path path) {
        boolean isDirectoryCanBeDeleted = false;
        try {
            Iterator<Path> directoryIterator = Files.newDirectoryStream(path).iterator();
            while (directoryIterator.hasNext()) {
                Path filePath = directoryIterator.next();
                if (Files.isDirectory(filePath) && isDirectoryCanBeDeleted(filePath)) {
                    return true;
                } else if (isFileCreatedTimeBefore(maxExpiryDays, filePath)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isDirectoryCanBeDeleted;
    }

    private boolean isFileCreatedTimeBefore(int numberOfDays, Path path) {
        BasicFileAttributes attributes;
        try {
            attributes = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Instant lastATI = attributes.creationTime().toInstant();
        LocalDate lastAccessTime = LocalDateTime.ofInstant(lastATI, ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.minus(numberOfDays, ChronoUnit.DAYS);

        return lastAccessTime.isBefore(expiryDate);
    }
}