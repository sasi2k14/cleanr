package sasi;

import java.nio.file.Path;

public interface Archivable {

	public void setArchivePath(Path path);
	public boolean arvhiveFile(Path path);
	public boolean arhiveFolder(Path path);
	public boolean deleteFile(Path path);
	public boolean deleteFolder(Path path);
	
}
