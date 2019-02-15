package sasi;

import sasi.filters.CleanFilter;
import sasi.filters.FileCreateTimeBasedFilter;
import sasi.filters.HiddenFilesFilter;
import sasi.filters.LastAccessTimeBasedFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Path;
import java.util.*;

public class Main {

	public static void main(String[] args) throws IOException {

		Map<CLIArgs, String> argsMap = null;

		try {
			argsMap = convertArgsToMap(args);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			printUsage();
			return;
		}

		String rootPath = argsMap.get(CLIArgs.path);
		String dryRunArg = argsMap.getOrDefault(CLIArgs.dryrun, "false");
		String maxDaysArg = argsMap.getOrDefault(CLIArgs.maxdays, "30");

		boolean dryRun  = Boolean.valueOf(dryRunArg);
		int maxDays = Integer.valueOf(maxDaysArg);
    	Path start = new File(rootPath).toPath();

		Path archivePath = Utils.getAndCreateArchiveDirectoryPath(new File(rootPath).toPath());
		System.out.println("Archive dir: " + archivePath);

		Cleanr archivesCleanr = new Cleanr(archivePath);
		archivesCleanr.addFilter(new FileCreateTimeBasedFilter());
		List<Path> oldFiles = archivesCleanr.getFilesForCleanUp();

		System.out.println("Total for cleanup: " + oldFiles.size());
		for (Path oldFile : oldFiles) {
			if(!dryRun){
				try {
					FileCleanr.deleteFile(oldFile);
				} catch (DirectoryNotEmptyException ex){
					System.out.println("Directory not empty " + oldFile + " cannot be deleted.");
				}
				System.out.println(oldFile + " deleted.");
			} else {
				System.out.println(oldFile + " to be deleted.");
			}
		}

		Cleanr cleanr = new Cleanr(start);
		List<Path> filesForArchival = cleanr.getFilesForCleanUp();
		System.out.println("Total files for archival: " + filesForArchival.size());

		for (Path path : filesForArchival) {
			if(!dryRun) {
				FileCleanr.moveFile(path, archivePath);
				System.out.println(path + " archived.");
			} else {
				System.out.println(path + " to be archived.");
			}
		}


    }

	private static Map<CLIArgs, String> convertArgsToMap(String[] args) throws IllegalArgumentException {
		Map<CLIArgs, String> result = new HashMap<>();

		if(args.length == 0) {
			throw new IllegalArgumentException("Arguments cannot be empty");
		}

		for(String arg : args) {
			StringTokenizer tokenizer = new StringTokenizer(arg, "=");
			if(tokenizer.countTokens() != 2) {
				throw new IllegalArgumentException("Argument" + arg + " is not valid");
			}

			try {
				CLIArgs cliArg = CLIArgs.valueOf(tokenizer.nextToken());
				result.put(cliArg, tokenizer.nextToken());
			} catch (Exception e) {
				throw new IllegalArgumentException("Argument" + arg + " is not valid");
			}
		}

		return result;
	}

	private static void printUsage() {
		System.out.println("Arguments are invalid");
		System.out.println("java -jar cleanr.jar path=path-to-scan-and-clean [dryRun=true|false] [maxdays=[any number]]");
		System.out.println("e.g java -jar cleanr.jar path=/path/ dryRun=true maxdays=7]");
	}

}
