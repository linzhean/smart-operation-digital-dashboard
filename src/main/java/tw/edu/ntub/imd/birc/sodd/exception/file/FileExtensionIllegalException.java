package tw.edu.ntub.imd.birc.sodd.exception.file;

public class FileExtensionIllegalException extends FileException {
    public FileExtensionIllegalException(String message) {
        super(message);
    }

    @Override
    public String getReason() {
        return "FileExtensionIllegal";
    }
}
