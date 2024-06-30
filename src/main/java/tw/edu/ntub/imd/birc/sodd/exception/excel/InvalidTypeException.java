package tw.edu.ntub.imd.birc.sodd.exception.excel;

import java.nio.file.Path;

public class InvalidTypeException extends ExcelException {
    public InvalidTypeException(Path path, String targetType) {
        super("格式錯誤，應為" + targetType + "(若Excel檔案副檔名為xls，則應使用XLSWorkbook): " + path);
    }

    @Override
    public String getReason() {
        return "InvalidType";
    }
}
