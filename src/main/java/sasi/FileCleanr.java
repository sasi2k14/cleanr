package sasi;

import java.nio.file.Path;

public class FileCleanr implements Archivable {	

	private Path archivePath;

	@Override
	public boolean arvhiveFile(Path path) {
		return false;
	}

	@Override
	public boolean arhiveFolder(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setArchivePath(Path path) {
		archivePath = path;
	}

	@Override
	public boolean deleteFile(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFolder(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

}
