package sasi.filters;

import java.nio.file.Path;

/**
 * Class would cause hidden files not to be deleted.
 */
public class HiddenFilesFilter implements CleanFilter {

    @Override
    public boolean isPathCanBeDeleted(Path path) {
        if(path.toFile().isHidden()) {
            return false;
        }
        return true;
    }


}
