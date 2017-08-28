package sasi;

import sasi.filters.ApplicationFilesExclusionFilter;
import sasi.filters.CleanFilter;
import sasi.filters.HiddenFilesFilter;
import sasi.filters.LastAccessTimeFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String args[]) throws IOException {
//		String rootPath = "/Users/qgb368/workspace/personal/cleanr/temp";
		String rootPath = "/Users/qgb368/Downloads";
    	Path start = new File(rootPath).toPath();

    	List<CleanFilter> filters = new ArrayList<>();
    	filters.add(new LastAccessTimeFilter());
    	filters.add(new HiddenFilesFilter());

    	Cleanr cleanr = new Cleanr();

    	List<Path> excludedPaths = new ArrayList<>();
    	excludedPaths.add(Utils.getArchiveDirectoryPath(start));
    	excludedPaths.add(start);
    	cleanr.addFilter(new ApplicationFilesExclusionFilter(excludedPaths));

    	List<Path> filesForArchival = cleanr.scanFiles(new File(rootPath).toPath());

		System.out.println("Total: " + filesForArchival.size());

		Path archivePath = Utils.getAndCreateArchiveDirectoryPath(new File(rootPath).toPath());
		System.out.println("Archive dir: " + archivePath);

		for (Path path : filesForArchival) {
			System.out.println(path);
			FileCleanr.moveFile(path, archivePath);
		}

		List<Path> oldFiles = cleanr.scanFiles(archivePath);
		System.out.println("Total: " + oldFiles.size());
		for (Path oldFile : oldFiles) {
			FileCleanr.deleteFile(oldFile);
			System.out.println(oldFile);
		}

    }
	
}
