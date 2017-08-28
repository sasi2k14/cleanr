package sasi.filters;

import sasi.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class LastAccessTimeFilter implements CleanFilter {

	@Override
	public boolean isPathCanBeDeleted(Path path) {
		if(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) ){
			return isLastAccessTimeBefore(30, path);
		} else {
			return isDirectoryCanBeDeleted(path);
		}
	}

	private boolean isDirectoryCanBeDeleted(Path path) {
		boolean isDirectoryCanBeDeleted = false;
		try {
			Iterator<Path> directoryIterator = Files.newDirectoryStream(path).iterator();
			while(directoryIterator.hasNext()){
				Path filePath = directoryIterator.next();
				if(Files.isDirectory(filePath) && isDirectoryCanBeDeleted(filePath)) {
					return true;
				}
				else if( isLastAccessTimeBefore(30, filePath) ) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isDirectoryCanBeDeleted;
	}

	private boolean isLastAccessTimeBefore(int numberOfDays, Path path) {
		BasicFileAttributes attributes;
		try {
			attributes = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Instant lastATI = attributes.lastAccessTime().toInstant();
		LocalDate lastAccessTime = LocalDateTime.ofInstant(lastATI, ZoneId.systemDefault()).toLocalDate();

		LocalDate today = LocalDate.now();
		LocalDate date30daysBack = today.minus(numberOfDays, ChronoUnit.DAYS);

		return lastAccessTime.isBefore(date30daysBack);
	}
}
