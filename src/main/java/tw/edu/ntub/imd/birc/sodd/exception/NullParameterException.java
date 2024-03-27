package tw.edu.ntub.imd.birc.sodd.exception;

public class NullParameterException extends BircException {
    public NullParameterException() {
        super("輸入參數為 null");
    }

    public NullParameterException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "Parameter - Null";
    }

}
