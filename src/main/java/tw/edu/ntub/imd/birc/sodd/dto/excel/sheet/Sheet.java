package tw.edu.ntub.imd.birc.sodd.dto.excel.sheet;

import tw.edu.ntub.birc.common.wrapper.date.DateTimePattern;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.Column;
import tw.edu.ntub.imd.birc.sodd.dto.excel.function.ExcelFunction;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.util.excel.ExcelUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SuppressWarnings("unused")
public interface Sheet {
    Object getOriginalObject();

    Workbook getWorkbook();

    String getName();

    default boolean isNameEquals(String sheetName) {
        String name = getName();
        return name.equals(sheetName);
    }

    void setName(String name);

    int getIndex();

    List<Row> getLoadedRowList();

    default Column getColumn(int columnIndex) {
        return getColumn(ExcelUtils.formatColumnIndexToEnglish(columnIndex));
    }

    Column getColumn(String columnName);

    default Row getRowByRowNumber(int rowNumber) {
        return getRowByRowIndex(rowNumber - 1);
    }

    Row getRowByRowIndex(int rowIndex);

    Cell getCell(String cellName);

    default Cell getCellByRowNumber(int columnIndex, int rowNumber) {
        Row row = getRowByRowNumber(rowNumber);
        return row.getCell(columnIndex);
    }

    default Cell getCellByRowNumber(String columnName, int rowNumber) {
        Row row = getRowByRowNumber(rowNumber);
        return row.getCell(columnName);
    }

    default Cell getCellByRowIndex(int columnIndex, int rowIndex) {
        Row row = getRowByRowIndex(rowIndex);
        return row.getCell(columnIndex);
    }

    default Cell getCellByRowIndex(String columnName, int rowIndex) {
        Row row = getRowByRowIndex(rowIndex);
        return row.getCell(columnName);
    }

    default void setCellValue(String cellName, @Nullable String value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable Integer value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable Long value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable Double value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable Boolean value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable LocalDate value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable LocalTime value, @Nullable DateTimePattern pattern) {
        Cell cell = getCell(cellName);
        cell.setValue(value, pattern);
    }

    default void setCellValue(String cellName, @Nullable LocalDateTime value) {
        Cell cell = getCell(cellName);
        cell.setValue(value);
    }

    default void setCellValue(String cellName, @Nullable ExcelFunction function) {
        Cell cell = getCell(cellName);
        cell.setValue(function);
    }

    default void clearRowContentByRowNumber(int rowNumber) {
        Row row = getRowByRowNumber(rowNumber);
        row.clear();
    }

    default void clearRowContentByRowIndex(int rowIndex) {
        Row row = getRowByRowIndex(rowIndex);
        row.clear();
    }

    void clearColumnContent(int columnIndex);

    void clearColumnContent(String columnName);
}
