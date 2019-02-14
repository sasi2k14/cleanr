package sasi;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class FileCleanrTest {

    @Test
    public void testIfEmptyDirectoryCanBeMoved() throws IOException {
        String src = "/tmp/cleanr-src";
        Path srcPath = Paths.get(src, "" + LocalDateTime.now());
        Files.createDirectories(srcPath);

        System.out.println(srcPath + " created.");

        String dest = "/tmp/cleanr-dest";
        Path destPath = Paths.get(dest);

        FileCleanr.moveFile(srcPath, destPath);

        Assert.assertTrue(destPath.toFile().exists());
        Assert.assertTrue(destPath.toFile().isDirectory());
    }

    @Test
    public void testIfDirectoryWithFilesCanBeMoved() throws IOException {
        String src = "/tmp/cleanr-src";
        Path srcPath = Paths.get(src, "" + LocalDateTime.now());
        Files.createDirectories(srcPath);

        System.out.println(srcPath + " created.");

        Path file1 = Files.createTempFile("cleanr", "");
        Path file2 =Files.createTempFile("cleanr", "");

        Files.copy(file1, Paths.get(srcPath.toString(),file1.getFileName().toString()));
        Files.copy(file2, Paths.get(srcPath.toString(),file2.getFileName().toString()));

        String dest = "/tmp/cleanr-dest";
        Path destPath = Paths.get(dest);
        Files.createDirectories(destPath);

        FileCleanr.moveFile(srcPath, destPath);

        Assert.assertTrue(destPath.toFile().exists());
        Path movedDestPath = Paths.get(dest, srcPath.getFileName().toString());
        Assert.assertTrue(Files.list(movedDestPath).count() == 2);

    }
}