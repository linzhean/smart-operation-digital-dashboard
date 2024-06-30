package tw.edu.ntub.imd.birc.sodd.exception.excel;

public class SheetNameExistException extends ExcelException {
    public SheetNameExistException(String sheetName) {
        super("工作表名稱重複: " + sheetName);
    }

    @Override
    public String getReason() {
        return "SheetNameRepeat";
    }
}
