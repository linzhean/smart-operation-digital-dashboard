package tw.edu.ntub.imd.birc.sodd.exception.form;

import tw.edu.ntub.birc.common.exception.date.ParseDateException;

public class InvalidFormDateFormatException extends InvalidRequestFormatException {
    public InvalidFormDateFormatException(ParseDateException cause) {
        super(cause.getDateString() + "日期轉換失敗" + (cause.getPattern() != null ? ", 格式化字串為: " + cause.getPattern() : ""));
    }
}
