package tw.edu.ntub.imd.birc.sodd.dto.excel.row;

import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.range.Range;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.util.excel.ExcelUtils;

import java.util.List;

@SuppressWarnings("unused")
public interface Row extends Range {
    Object getOriginalObject();

    Workbook getWorkbook();

    Sheet getSheet();

    default int getNumber() {
        return getIndex() + 1;
    }

    int getIndex();

    List<Cell> getLoadedCellList();

    default Cell getCell(String columnName) {
        int cellIndex = ExcelUtils.formatColumnNameToColumnIndex(columnName);
        return getCell(cellIndex);
    }

    Cell getCell(int cellIndex);

    void clear();
}
