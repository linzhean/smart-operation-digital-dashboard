package tw.edu.ntub.imd.birc.sodd.exception;

import tw.edu.ntub.birc.common.exception.ProjectException;

public class PermissionDeniedException extends ProjectException {
    public PermissionDeniedException(Throwable cause) {
        super("權限不足", cause);
    }

    @Override
    public String getErrorCode() {
        return "PermissionDenied";
    }


}
