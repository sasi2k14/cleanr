package sasi;

import sasi.filters.CleanFilter;
import sasi.filters.HiddenFilesFilter;
import sasi.filters.LastAccessTimeBasedFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			printUsage();
			return;
		}
		String rootPath = args[0];
		boolean dryRun  = args[1] == null ? false : true;
    	Path start = new File(rootPath).toPath();

    	List<CleanFilter> filters = new ArrayList<>();
    	filters.add(new LastAccessTimeBasedFilter());
    	filters.add(new HiddenFilesFilter());

		Path archivePath = Utils.getAndCreateArchiveDirectoryPath(new File(rootPath).toPath());
		System.out.println("Archive dir: " + archivePath);

		Cleanr archivesCleanr = new Cleanr(archivePath);
		List<Path> oldFiles = archivesCleanr.getFilesForCleanUp();

		System.out.println("Total: " + oldFiles.size() + " to be moved to archives");
		for (Path oldFile : oldFiles) {
			if(!dryRun){
				FileCleanr.deleteFile(oldFile);
				System.out.println(oldFile + " deleted.");
			} else {
				System.out.println(oldFile + " to be deleted.");
			}
		}

		Cleanr cleanr = new Cleanr(start);
		List<Path> filesForArchival = cleanr.getFilesForCleanUp();
		System.out.println("Total: " + filesForArchival.size());

		for (Path path : filesForArchival) {
			if(!dryRun) {
				FileCleanr.moveFile(path, archivePath);
				System.out.println(path + " archived.");
			} else {
				System.out.println(path + " to be archived.");
			}
		}


    }

	private static void printUsage() {
		System.out.println("Arguments are invalid");
		System.out.println("cleanr <dir-to-cleanup> <dryRun>");
	}

}
