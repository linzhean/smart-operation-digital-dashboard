package tw.edu.ntub.imd.birc.sodd.exception.excel;

import tw.edu.ntub.birc.common.exception.ProjectException;

public abstract class ExcelException extends ProjectException {
    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "Excel - " + getReason();
    }

    public abstract String getReason();
}
