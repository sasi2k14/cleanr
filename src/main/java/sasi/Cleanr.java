package sasi;

import sasi.filters.CleanFilter;
import sasi.filters.HiddenFilesFilter;
import sasi.filters.LastAccessTimeFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sasikumar on 8/13/16.
 */
public class Cleanr {

    private static final boolean DO_NOT_DELETE = false;
    Configuration config;
    
    private List<CleanFilter> filters;

    private FileCleanr fileCleanr;

    public Cleanr(){
        filters = new ArrayList<>();
        filters.add(new LastAccessTimeFilter());
        filters.add(new HiddenFilesFilter());
    }

    public Cleanr(List<CleanFilter> _filters){
        filters = _filters;
    }

    public void addFilter(CleanFilter filter) {
        filters.add(filter);
    }

    public List<Path> scanFiles(Path root) {
    	try {
            Stream<Path> filesForDeletion = Files.find(root, 1,
                                            (path, attributes) -> isPathMarkedForDeletion(path));
            return filesForDeletion.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    protected boolean isPathMarkedForDeletion(Path path) {
        boolean canBeDeleted = true;
        for(CleanFilter filter : filters) {
                boolean result = filter.isPathCanBeDeleted(path);
                System.out.println(filter.getClass().getName() + " + " + path + "=" + result);
                if(result == DO_NOT_DELETE) {
                    return false;
                }
//                if(isFile(path)) {
//                    canBeDeleted = canBeDeleted && filter.isPathCanBeDeleted(path);
//                }
        }
        return canBeDeleted;
    }

    protected boolean isFile(Path path) {
        return Files.isRegularFile(path);
    }

}
