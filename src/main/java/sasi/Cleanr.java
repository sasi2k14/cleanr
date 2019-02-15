package sasi;

import sasi.filters.DefaultExcludedPathsFilter;
import sasi.filters.CleanFilter;
import sasi.filters.HiddenFilesFilter;
import sasi.filters.LastAccessTimeBasedFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sasikumar on 8/13/16.
 */
public class Cleanr {

    private static final boolean DO_NOT_DELETE = false;
    public static final int DIRECTORY_DEPTH = 1;

    private List<CleanFilter> filters = new ArrayList<>();
    private Path pathToCleanUp;

    private FileCleanr fileCleanr;

    public Cleanr(Path path){

        pathToCleanUp = path;

        List<Path> excludedPaths = new ArrayList<>();
        excludedPaths.add(Utils.getArchiveDirectoryPath(pathToCleanUp));
        excludedPaths.add(pathToCleanUp);

        addFilter(new DefaultExcludedPathsFilter(excludedPaths));
        addFilter(new HiddenFilesFilter());
    }

    public Cleanr(List<CleanFilter> _filters){
        filters = _filters;
    }

    public void addFilter(CleanFilter filter) {
        filters.add(filter);
    }

    public List<Path> getFilesForCleanUp() {
        if(pathToCleanUp.toFile().exists() == false) {
            System.out.println(pathToCleanUp + "Path is empty");
            return Collections.EMPTY_LIST;
        }

    	try {
            Stream<Path> filesForDeletion =
                                    Files.find(
                                            pathToCleanUp,
                                            DIRECTORY_DEPTH,
                                            (path, attributes) -> isPathMarkedForDeletion(path));
            return filesForDeletion.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    protected boolean isPathMarkedForDeletion(Path path) {
        for(CleanFilter filter : filters) {
            boolean result = filter.isPathCanBeCleaned(path);
            if(result == DO_NOT_DELETE) {
                System.out.println(path + " is marked NOT to be deleted by " + filter.getClass().getName());
                return false;
            }
        }
        System.out.println(path + " can be deleted");
        return true;
    }

}
