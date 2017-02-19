package sasi;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Main {

	public static void main(String args[]){
    	Path start = new File("/Users/QGB368/Downloads").toPath();
    	Cleanr cleanr = new Cleanr(start);
    	List<Path> filesForArchival = cleanr.scanFilesForArchival();
    	cleanr.clean(filesForArchival, false);
    	
    	List<Path> filesForDeletion = cleanr.scanArchivesDirectoryPath();
    	cleanr.clean(filesForDeletion, true);
    }
	
}