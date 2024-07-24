package tw.edu.ntub.imd.birc.sodd.exception.excel;


import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.enumerate.CellType;

public class InvalidCellValueFormatException extends ExcelException {
    public InvalidCellValueFormatException(Cell cell, CellType expectedType) {
        super(cell.getCellName() + "類型不是" + expectedType + "，類型為" + cell.getType());
    }

    @Override
    public String getReason() {
        return "InvalidType";
    }
}
