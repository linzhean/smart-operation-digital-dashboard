package tw.edu.ntub.imd.birc.sodd.exception;

public class DateParseException extends BircException {
    public DateParseException(String dateString) {
        super("日期轉換失敗，字串為：" + dateString);
    }

    public DateParseException(String dateString, String pattern) {
        super("日期轉換失敗，字串為：" + dateString + "，日期格式為：" + pattern);
    }

    @Override
    public String getErrorCode() {
        return "Date - ParseError";
    }
}
