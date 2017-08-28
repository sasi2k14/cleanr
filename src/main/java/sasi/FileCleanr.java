package sasi;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Deletes or Archives files
 */
public class FileCleanr {

	public static void moveFile(Path src, Path dest) throws IOException {
        dest = dest.resolve(src.getFileName());
        Files.move(src, dest, StandardCopyOption.ATOMIC_MOVE);
	}

	public static boolean deleteFile(Path path) throws IOException {
		return Files.deleteIfExists(path);
	}

}
