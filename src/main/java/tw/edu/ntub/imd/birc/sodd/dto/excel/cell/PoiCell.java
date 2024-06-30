package tw.edu.ntub.imd.birc.sodd.dto.excel.cell;

import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import tw.edu.ntub.birc.common.wrapper.date.DateTimePattern;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.Column;
import tw.edu.ntub.imd.birc.sodd.dto.excel.function.ExcelFunction;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.enumerate.CellType;
import tw.edu.ntub.imd.birc.sodd.exception.excel.CellValueErrorException;
import tw.edu.ntub.imd.birc.sodd.exception.excel.InvalidCellValueFormatException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PoiCell implements Cell {
    private final Column column;
    private final Row row;
    private final org.apache.poi.ss.usermodel.Cell cell;
    private final FormulaEvaluator formulaEvaluator;
    private CellType type;
    private ExcelFunction functionValue;

    public PoiCell(
            Column column,
            Row row,
            org.apache.poi.ss.usermodel.Cell cell,
            FormulaEvaluator formulaEvaluator
    ) {
        this.column = column;
        this.row = row;
        this.cell = cell;
        this.formulaEvaluator = formulaEvaluator;
        this.type = getCellTypeFromPoi(cell);
    }

    private CellType getCellTypeFromPoi(org.apache.poi.ss.usermodel.Cell cell) {
        switch (cell.getCellType()) {
            case _NONE:
            case BLANK:
            case STRING:
                return CellType.STRING;
            case NUMERIC:
                if (cell.getNumericCellValue() == (int) cell.getNumericCellValue()) {
                    return CellType.INT;
                }
                return CellType.DOUBLE;
            case FORMULA:
                return CellType.FUNCTION;
            case BOOLEAN:
                return CellType.BOOLEAN;
            case ERROR:
                return CellType.ERROR;
            default:
                throw new IllegalStateException("未知儲存格類型: " + cell.getCellType());
        }
    }

    @Override
    public org.apache.poi.ss.usermodel.Cell getOriginalObject() {
        return cell;
    }

    @Override
    public Workbook getWorkbook() {
        return column.getWorkbook();
    }

    @Override
    public Sheet getSheet() {
        return column.getSheet();
    }

    @Override
    public Column getColumn() {
        return column;
    }

    @Override
    public Row getRow() {
        return row;
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        CellValue formulaValue = formulaEvaluator.evaluate(cell);
        if (formulaValue.getCellType() == org.apache.poi.ss.usermodel.CellType.ERROR) {
            CellValue errorValue = CellValue.getError(formulaValue.getErrorValue());
            return errorValue.formatAsString();
        }
        if (formulaValue.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
            return new BigDecimal(formulaValue.getNumberValue()).toString();
        }
        return formulaValue.formatAsString();
    }

    @Nonnull
    @Override
    public String getDataEditorValue() {
        return cell.getCellFormula();
    }

    @Override
    public int getValueAsInt() {
        CellValue formulaValue = formulaEvaluator.evaluate(cell);
        switch (formulaValue.getCellType()) {
            case STRING:
                return Integer.parseInt(formulaValue.getStringValue());
            case NUMERIC:
                return (int) formulaValue.getNumberValue();
            case ERROR:
                CellValue errorValue = CellValue.getError(formulaValue.getErrorValue());
                throw new CellValueErrorException(errorValue.formatAsString());
            case FORMULA:
            case _NONE:
            case BLANK:
            case BOOLEAN:
            default:
                throw new InvalidCellValueFormatException(this, CellType.DOUBLE);
        }
    }

    @Override
    public long getValueAsLong() {
        CellValue formulaValue = formulaEvaluator.evaluate(cell);
        switch (formulaValue.getCellType()) {
            case NUMERIC:
            case STRING:
                return Long.parseLong(formulaValue.formatAsString());
            case ERROR:
                CellValue errorValue = CellValue.getError(formulaValue.getErrorValue());
                throw new CellValueErrorException(errorValue.formatAsString());
            case FORMULA:
            case _NONE:
            case BLANK:
            case BOOLEAN:
            default:
                throw new InvalidCellValueFormatException(this, CellType.DOUBLE);
        }
    }

    @Override
    public double getValueAsDouble() {
        CellValue formulaValue = formulaEvaluator.evaluate(cell);
        switch (formulaValue.getCellType()) {
            case NUMERIC:
                return formulaValue.getNumberValue();
            case STRING:
                return Double.parseDouble(formulaValue.formatAsString());
            case ERROR:
                CellValue errorValue = CellValue.getError(formulaValue.getErrorValue());
                throw new CellValueErrorException(errorValue.formatAsString());
            case FORMULA:
            case _NONE:
            case BLANK:
            case BOOLEAN:
            default:
                throw new InvalidCellValueFormatException(this, CellType.DOUBLE);
        }
    }

    @Override
    public boolean getValueAsBoolean() {
        CellValue formulaValue = formulaEvaluator.evaluate(cell);
        if (formulaValue.getCellType() == org.apache.poi.ss.usermodel.CellType.ERROR) {
            CellValue errorValue = CellValue.getError(formulaValue.getErrorValue());
            throw new CellValueErrorException(errorValue.formatAsString());
        }
        return formulaValue.getBooleanValue();
    }

    @Nullable
    @Override
    public LocalDate getValueAsLocalDate() {
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            return LocalDate.parse(cell.getStringCellValue());
        }
        LocalDateTime valueAsLocalDateTime = getValueAsLocalDateTime();
        if (valueAsLocalDateTime != null) {
            return valueAsLocalDateTime.toLocalDate();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public LocalTime getValueAsLocalTime() {
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            return LocalTime.parse(cell.getStringCellValue());
        }
        LocalDateTime valueAsLocalDateTime = getValueAsLocalDateTime();
        if (valueAsLocalDateTime != null) {
            return valueAsLocalDateTime.toLocalTime();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public LocalDateTime getValueAsLocalDateTime() {
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            return LocalDateTime.parse(cell.getStringCellValue());
        }
        return cell.getLocalDateTimeCellValue();
    }

    @Override
    public ExcelFunction getValueAsFunction() {
        return functionValue;
    }

    @Override
    public void setValue(@Nullable String value) {
        cell.setCellValue(value);
        type = CellType.STRING;
    }

    @Override
    public void setValue(@Nullable Integer value) {
        if (value != null) {
            cell.setCellValue(value);
            type = CellType.INT;
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable Long value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable Double value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable Boolean value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable LocalDate value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable LocalTime value, @Nullable DateTimePattern pattern) {
        if (value != null && pattern != null) {
            cell.setCellValue(value.format(DateTimeFormatter.ofPattern(pattern.getPattern(), pattern.getLocale())));
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable LocalDateTime value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            clear();
        }
    }

    @Override
    public void setValue(@Nullable ExcelFunction function) {
        if (function != null) {
            cell.setCellFormula(function.getFunctionDefineString());
            this.functionValue = function;
        } else {
            cell.setBlank();
        }
    }

    @Override
    public void clear() {
        cell.setBlank();
        type = CellType.STRING;
    }

    @Override
    public CellType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getCellName();
    }
}
