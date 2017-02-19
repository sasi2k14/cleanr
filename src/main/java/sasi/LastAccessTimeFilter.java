package sasi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class LastAccessTimeFilter implements CleanFilter {

	@Override
	public boolean filterRegularFile(Path path) {
		BasicFileAttributes attributes;
		try {
			attributes = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		FileTime now = FileTime.from(Instant.now());
    	long daysfromEpoch = now.to(TimeUnit.DAYS);
    	daysfromEpoch -= 30;
    	
    	FileTime thirtyDatesBack = FileTime.from(daysfromEpoch, TimeUnit.DAYS);
    	
    	System.out.println("30 days back: " + thirtyDatesBack);
    	System.out.printf("Checking file %s , with time %s\n", path, attributes.lastAccessTime());
    	
    	if(attributes.lastAccessTime().compareTo(thirtyDatesBack) > -1) {
    		return true;
    	}
    	return false;
	}

	@Override
	public boolean filterDirectory(Path path) {
		return false;
	}

}
