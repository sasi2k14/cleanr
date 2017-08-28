package sasi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import sasi.filters.CleanFilter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

/**
 * Created by qgb368 on 8/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CleanrServiceTest {

    @Mock
    private Path path;

    private List<CleanFilter> filters;

    @Test public void failEvenOneFilterReturnsFalse() throws IOException {
        List<CleanFilter> cleanFilters = new ArrayList<>();

        CleanFilter trueFilter = mock(CleanFilter.class);
        CleanFilter falseFilter = Mockito.mock(CleanFilter.class);

        when(trueFilter.isPathCanBeDeleted(any())).thenReturn(true);
        when(falseFilter.isPathCanBeDeleted(any())).thenReturn(false);

        cleanFilters.add(trueFilter);
        cleanFilters.add(falseFilter);
        cleanFilters.add(trueFilter);

        Cleanr cleanr = new Cleanr(cleanFilters);
        cleanr = spy(cleanr);
        doReturn(true).when(cleanr).isFile(any());

        boolean result = cleanr.isPathMarkedForDeletion(Paths.get(""));
        Assert.assertFalse(result);

    }

    @Test public void passWhenAllFilterReturnsTrue() throws IOException {
        List<CleanFilter> cleanFilters = new ArrayList<>();

        CleanFilter trueFilter = mock(CleanFilter.class);
        CleanFilter falseFilter = Mockito.mock(CleanFilter.class);

        when(trueFilter.isPathCanBeDeleted(any())).thenReturn(true);
        when(falseFilter.isPathCanBeDeleted(any())).thenReturn(false);

        cleanFilters.add(trueFilter);
        cleanFilters.add(trueFilter);
        cleanFilters.add(trueFilter);

        Cleanr cleanr = new Cleanr(cleanFilters);
        cleanr = spy(cleanr);
        doReturn(true).when(cleanr).isFile(any());

        System.out.println("Total filters:" + cleanFilters.size());
        boolean result = cleanr.isPathMarkedForDeletion(null);
        Assert.assertTrue(result);
    }

    @Test public void failWhenAllFilterReturnsFalse() throws IOException {
        List<CleanFilter> cleanFilters = new ArrayList<>();

        CleanFilter trueFilter = mock(CleanFilter.class);
        CleanFilter falseFilter = Mockito.mock(CleanFilter.class);

        when(falseFilter.isPathCanBeDeleted(any())).thenReturn(false);

        cleanFilters.add(falseFilter);
        cleanFilters.add(falseFilter);
        cleanFilters.add(falseFilter);

        Cleanr cleanr = new Cleanr(cleanFilters);
        cleanr = spy(cleanr);
        doReturn(true).when(cleanr).isFile(any());

        System.out.println("Total filters:" + cleanFilters.size());
        boolean result = cleanr.isPathMarkedForDeletion(null);
        Assert.assertFalse(result);
    }

}
