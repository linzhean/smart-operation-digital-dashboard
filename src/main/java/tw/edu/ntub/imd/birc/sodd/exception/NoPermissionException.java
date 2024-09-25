package tw.edu.ntub.imd.birc.sodd.exception;

import tw.edu.ntub.birc.common.exception.ProjectException;

public class NoPermissionException extends ProjectException {

    public NoPermissionException(String chartName) {
        super("您無法添加" + chartName + "至您的儀表板");
    }

    public NoPermissionException() {
        super("您並無針對此圖表發送交辦事項的權限");
    }

    @Override
    public String getErrorCode() {
        return "NoPermission";
    }
}
