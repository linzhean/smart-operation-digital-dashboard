package tw.edu.ntub.imd.birc.sodd.dto.excel.row;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.PoiCell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.PoiColumn;
import tw.edu.ntub.imd.birc.sodd.dto.excel.range.Range;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.enumerate.RangeFixType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PoiRow implements Row {
    private final Sheet activeSheet;
    private final org.apache.poi.ss.usermodel.Row row;
    private final FormulaEvaluator formulaEvaluator;
    private final List<Cell> cellList = new ArrayList<>();
    private final Range rowRange;

    public PoiRow(Sheet activeSheet, org.apache.poi.ss.usermodel.Row row, FormulaEvaluator formulaEvaluator) {
        this.activeSheet = activeSheet;
        this.row = row;
        this.formulaEvaluator = formulaEvaluator;
        for (org.apache.poi.ss.usermodel.Cell cell : row) {
            cellList.add(new PoiCell(
                    new PoiColumn(activeSheet, cell.getColumnIndex()),
                    this,
                    cell,
                    formulaEvaluator
            ));
        }
        this.rowRange = Range.createRowRange(getNumber());
    }

    @Override
    public org.apache.poi.ss.usermodel.Row getOriginalObject() {
        return row;
    }

    @Override
    public Workbook getWorkbook() {
        return activeSheet.getWorkbook();
    }

    @Override
    public Sheet getSheet() {
        return activeSheet;
    }

    @Override
    public int getIndex() {
        return row.getRowNum();
    }

    @Override
    public List<Cell> getLoadedCellList() {
        return Collections.unmodifiableList(cellList);
    }

    @Override
    public Cell getCell(int cellIndex) {
        Optional<Cell> optionalCell = cellList.stream()
                .filter(cell -> cell.getColumnIndex() == cellIndex)
                .findFirst();
        if (optionalCell.isPresent()) {
            return optionalCell.get();
        } else {
            Cell cell = createNewCell(cellIndex);
            cellList.add(cell);
            return cell;
        }
    }

    private Cell createNewCell(int cellIndex) {
        return new PoiCell(
                new PoiColumn(activeSheet, cellIndex),
                this,
                row.createCell(cellIndex),
                formulaEvaluator
        );
    }

    @Override
    public void clear() {
        cellList.parallelStream().forEach(Cell::clear);
    }

    @Override
    public String getRange() {
        return rowRange.getRange();
    }

    @Override
    public void setFixType(RangeFixType type) {
        rowRange.setFixType(type);
    }

    @Override
    public String toString() {
        return String.valueOf(getNumber());
    }
}
