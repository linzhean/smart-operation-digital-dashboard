package tw.edu.ntub.imd.birc.sodd.exception.file;

import java.util.Arrays;

public class InvalidPathException extends FileException {

    public InvalidPathException(Throwable cause, String firstPath, String... subPathArray) {
        super("包含不合法的字元: " + firstPath + (subPathArray.length > 0 ? ", " + Arrays.toString(subPathArray) : ""), cause);
    }

    @Override
    public String getReason() {
        return "InvalidPath";
    }
}
