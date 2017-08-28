package sasi.filters;

import java.io.IOException;
import java.nio.file.Path;

public interface CleanFilter {
	public boolean isPathCanBeDeleted(Path path);
}
