package tw.edu.ntub.imd.birc.sodd.exception;

import tw.edu.ntub.birc.common.exception.ProjectException;

public class ChartException extends ProjectException {
    public ChartException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "Chart";
    }
}
