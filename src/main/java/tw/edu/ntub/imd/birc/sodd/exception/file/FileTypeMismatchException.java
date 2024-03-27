package tw.edu.ntub.imd.birc.sodd.exception.file;

public class FileTypeMismatchException extends FileException {
    public FileTypeMismatchException(String error) {
        super(error);
    }

    @Override
    public String getReason() {
        return "TypeMismatch";
    }
}
