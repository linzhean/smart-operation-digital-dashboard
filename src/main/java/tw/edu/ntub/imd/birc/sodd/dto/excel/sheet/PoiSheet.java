package tw.edu.ntub.imd.birc.sodd.dto.excel.sheet;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.Column;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.PoiColumn;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.PoiRow;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.PoiWorkbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class PoiSheet implements Sheet {
    private final PoiWorkbook workbook;
    private final org.apache.poi.ss.usermodel.Sheet sheet;
    private final FormulaEvaluator formulaEvaluator;
    private final List<PoiColumn> columnList = new ArrayList<>();
    private final List<Row> rowList = new ArrayList<>();

    public PoiSheet(PoiWorkbook workbook, org.apache.poi.ss.usermodel.Sheet sheet, FormulaEvaluator formulaEvaluator) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.formulaEvaluator = formulaEvaluator;
        for (org.apache.poi.ss.usermodel.Row row : sheet) {
            rowList.add(new PoiRow(this, row, formulaEvaluator));
        }
    }

    @Override
    public org.apache.poi.ss.usermodel.Sheet getOriginalObject() {
        return sheet;
    }

    @Override
    public PoiWorkbook getWorkbook() {
        return workbook;
    }

    @Override
    public String getName() {
        return sheet.getSheetName();
    }

    @Override
    public void setName(String name) {
        Workbook poiWorkbook = workbook.getOriginalObject();
        poiWorkbook.setSheetName(getIndex(), name);
    }

    @Override
    public int getIndex() {
        Workbook poiWorkbook = workbook.getOriginalObject();
        return poiWorkbook.getSheetIndex(getName());
    }

    @Override
    public List<Row> getLoadedRowList() {
        return Collections.unmodifiableList(rowList);
    }

    @Override
    public Column getColumn(String columnName) {
        Optional<PoiColumn> optionalColumn = columnList.parallelStream()
                .filter(column -> column.isNameEquals(columnName))
                .findFirst();
        if (optionalColumn.isPresent()) {
            return optionalColumn.get();
        } else {
            PoiColumn column = new PoiColumn(this, columnName);
            columnList.add(column);
            return column;
        }
    }

    @Override
    public Row getRowByRowIndex(int rowIndex) {
        Optional<Row> optionalRow = rowList.parallelStream()
                .filter(row -> row.getIndex() == rowIndex)
                .findFirst();
        if (optionalRow.isPresent()) {
            return optionalRow.get();
        } else {
            Row row = new PoiRow(this, sheet.createRow(rowIndex), formulaEvaluator);
            rowList.add(row);
            return row;
        }
    }

    @Override
    public Cell getCell(String cellName) {
        StringBuilder cellEnglish = new StringBuilder(3);
        int cellNumber = 0;
        char[] charArray = cellName.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char character = charArray[i];
            if (character >= '0' && character <= '9') {
                cellNumber = Integer.parseInt(cellName.substring(i));
                break;
            } else {
                cellEnglish.append(character);
            }
        }
        return getCellByRowNumber(cellEnglish.toString(), cellNumber);
    }

    @Override
    public void clearColumnContent(int columnIndex) {
        for (Row row : rowList) {
            for (Cell cell : row.getLoadedCellList()) {
                if (cell.getColumnIndex() == columnIndex) {
                    cell.clear();
                }
            }
        }
    }

    @Override
    public void clearColumnContent(String columnName) {
        for (Row row : rowList) {
            for (Cell cell : row.getLoadedCellList()) {
                String cellColumnName = cell.getColumnName();
                if (cellColumnName.equals(columnName)) {
                    cell.clear();
                }
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
