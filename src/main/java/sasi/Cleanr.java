package sasi;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sasikumar on 8/13/16.
 */
public class Cleanr {

    Configuration config;
    
    private Path cleanPath;
    private CleanFilter filter;
    private Archivable archiver = new FileCleanr();

    public Cleanr(Path path) {
    	cleanPath = path;
        Path archiveDirectoryPath = getArchivesDirectoryPath();
        archiver.setArchivePath(archiveDirectoryPath);
    }
    
    public Path getArchivesDirectoryPath() {
    	return cleanPath.resolve("cleanr-arvhives");
    }
    
    private List<Path> scanFilesForArchival(Path scanPath) {
    	try {
            Stream<Path> files = Files.find(scanPath, 1, (path, attributes) -> {
            	if(attributes.isRegularFile()){
            		return filter.filterRegularFile(path);
            	} else {
            		return filter.filterDirectory(path);
            	}
            });
            return files.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
    
    public List<Path> scanArchivesDirectoryPath() {
    	return scanFilesForArchival(getArchivesDirectoryPath());
    }
    
    public List<Path> scanFilesForArchival() {
        return scanFilesForArchival(cleanPath);
    }
    
    public void clean(List<Path> paths, boolean expunge){
    	paths.stream()
    			.filter(path -> path.toFile().isFile())
    			.forEach(path -> {
    				if(expunge) {
    					archiver.deleteFile(path);
    				} else {
    					archiver.arvhiveFile(path);
    				}
    			});
    }
    
    public static void main(String args[]){
    	Path start = new File("/Users/QGB368/Downloads").toPath();
    	Cleanr cleanr = new Cleanr(start);
    	List<Path> filesForArchival = cleanr.scanFilesForArchival();
    	cleanr.clean(filesForArchival, false);
    	
    	List<Path> filesForDeletion = cleanr.scanArchivesDirectoryPath();
    	cleanr.clean(filesForDeletion, true);
    }
}
