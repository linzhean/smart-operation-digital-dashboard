package tw.edu.ntub.imd.birc.sodd.exception.excel;

public class SheetNotFoundException extends ExcelException {
    public SheetNotFoundException(String sheetName) {
        super("找不到" + sheetName);
    }

    public SheetNotFoundException(int index) {
        super("找不到第" + (index + 1) + "個工作表");
    }

    @Override
    public String getReason() {
        return null;
    }
}
