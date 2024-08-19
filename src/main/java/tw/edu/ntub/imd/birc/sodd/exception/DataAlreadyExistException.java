package tw.edu.ntub.imd.birc.sodd.exception;

import tw.edu.ntub.birc.common.exception.ProjectException;

public class DataAlreadyExistException extends ProjectException {
    public DataAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "Data already existed";
    }
}
