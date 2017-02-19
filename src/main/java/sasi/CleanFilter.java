package sasi;

import java.nio.file.Path;

public interface CleanFilter {
	public boolean filterRegularFile(Path path);
	public boolean filterDirectory(Path path);
}
