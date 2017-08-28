package sasi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

	public static Path getArchiveDirectoryPath(Path rootPath) {
//    	Path archiveParent = rootPatth.getParent();
    	return rootPath.resolve("cleanr-arvhives");
    }

	public static Path getAndCreateArchiveDirectoryPath(Path rootPath) throws IOException {
		Path archivePath = getArchiveDirectoryPath(rootPath);
		return Files.createDirectories(archivePath);
	}
}
