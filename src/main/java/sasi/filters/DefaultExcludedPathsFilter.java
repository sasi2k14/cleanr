package sasi.filters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qgb368 on 8/27/17.
 */
public class DefaultExcludedPathsFilter implements CleanFilter {

    private List<Path> excludedPaths;

    public DefaultExcludedPathsFilter(List<Path> _excludedPaths) {
        excludedPaths = _excludedPaths;
    }

    @Override
    public boolean isPathCanBeCleaned(Path path) {
        if(excludedPaths.contains(path)) {
            return false;
        }
        return true;
    }

}
