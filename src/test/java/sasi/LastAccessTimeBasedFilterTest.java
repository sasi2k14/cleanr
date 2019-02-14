package sasi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sasi.filters.LastAccessTimeBasedFilter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by qgb368 on 3/20/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LastAccessTimeBasedFilter.class)
public class LastAccessTimeBasedFilterTest {

    @Mock BasicFileAttributeView basicFileAttributeView;

    @Mock BasicFileAttributes basicFileAttributes;

    @Mock Path path;

    @Before public void setup() throws IOException {
        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.isRegularFile(any(), any())).thenReturn(true);
        PowerMockito.when(Files.getFileAttributeView(any(),any())).thenReturn(basicFileAttributeView);

        Mockito.when(basicFileAttributeView.readAttributes()).thenReturn(basicFileAttributes);

    }

    @Test public void checkIfFileOlderThan30DaysCanBeDeleted() throws Exception {
        //Given
        Instant date30daysBack = Instant.now().minus(40, ChronoUnit.DAYS);
        Mockito.when(basicFileAttributes.lastAccessTime()).thenReturn(FileTime.from(date30daysBack));

        //When
        Assert.assertTrue( new LastAccessTimeBasedFilter().isPathCanBeCleaned(null) );
    }

    @Test public void checkIfFileEqualTo30DaysCannotBeDeleted() throws Exception {
        //Given
        Instant date30daysBack = Instant.now().minus(30, ChronoUnit.DAYS);
        Mockito.when(basicFileAttributes.lastAccessTime()).thenReturn(FileTime.from(date30daysBack));

        //When
        Assert.assertFalse( new LastAccessTimeBasedFilter().isPathCanBeCleaned(null) );
    }

    @Test public void checkIfFileOlderWithin30DaysCannotBeDeleted() throws Exception {
        //Given
        Instant date30daysBack = Instant.now().minus(10, ChronoUnit.DAYS);
        Mockito.when(basicFileAttributes.lastAccessTime()).thenReturn(FileTime.from(date30daysBack));

        //When
        Assert.assertFalse( new LastAccessTimeBasedFilter().isPathCanBeCleaned(null) );
    }

    @Ignore @Test public void checkAllFilesAreAccesedInsideFolder() throws URISyntaxException, IOException {
        File currentDir = new File(".");
        Path targetPath = Paths.get("src","test", "resources", "level1", "level2", "level2.file");
        targetPath = currentDir.toPath().resolve(targetPath);
        System.out.println(targetPath.toAbsolutePath());

        BasicFileAttributeView attributes = Files.getFileAttributeView(targetPath, BasicFileAttributeView.class);
        BasicFileAttributes targetAttribute = attributes.readAttributes();

        System.out.println("Before change:" + targetAttribute.lastAccessTime());
        FileTime targetTime = FileTime.from(Instant.now().minus(45, ChronoUnit.DAYS));
        attributes.setTimes(null, targetTime, null);

        System.out.println("After change:" + targetAttribute.lastAccessTime());

//        new LastAccessTimeBasedFilter().isDirectoryCanBeDeleted(Paths.get(new URI("file:///Users/qgb368/Downloads/src")));
    }
}
