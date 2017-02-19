package sasi;

import java.nio.file.Path;

public class Utils {
	public static Path getArchiveDirectoryPath(Path path) {
    	Path archiveParent = path.getParent();
    	return archiveParent.resolve("cleanr-arvhives");
    }
}
