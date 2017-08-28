package sasi.filters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qgb368 on 8/27/17.
 */
public class ApplicationFilesExclusionFilter implements CleanFilter {

    private List<Path> excludedPaths;

    public ApplicationFilesExclusionFilter(List<Path> _excludedPaths) {
        excludedPaths = _excludedPaths;
    }

    @Override
    public boolean isPathCanBeDeleted(Path path) {
        if(excludedPaths.contains(path)) {
            return false;
        }
        return true;
    }

}
