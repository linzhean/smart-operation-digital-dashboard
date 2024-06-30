package tw.edu.ntub.imd.birc.sodd.dto.excel.cell;

import tw.edu.ntub.birc.common.wrapper.date.DateTimePattern;
import tw.edu.ntub.imd.birc.sodd.dto.excel.column.Column;
import tw.edu.ntub.imd.birc.sodd.dto.excel.function.ExcelFunction;
import tw.edu.ntub.imd.birc.sodd.dto.excel.function.Reference;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.enumerate.CellType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("unused")
public interface Cell extends Reference {
    Object getOriginalObject();

    Workbook getWorkbook();

    Sheet getSheet();

    Column getColumn();

    default int getColumnIndex() {
        Column column = getColumn();
        return column.getIndex();
    }

    default String getColumnName() {
        Column column = getColumn();
        return column.getName();
    }

    Row getRow();

    default int getRowNumber() {
        Row row = getRow();
        return row.getNumber();
    }

    default int getRowIndex() {
        Row row = getRow();
        return row.getIndex();
    }

    default String getCellName() {
        return getColumnName() + getRowNumber();
    }

    @Nonnull
    String getValueAsString();

    @Nonnull
    String getDataEditorValue();

    int getValueAsInt();

    long getValueAsLong();

    double getValueAsDouble();

    boolean getValueAsBoolean();

    @Nullable
    LocalDate getValueAsLocalDate();

    @Nullable
    LocalTime getValueAsLocalTime();

    @Nullable
    LocalDateTime getValueAsLocalDateTime();

    ExcelFunction getValueAsFunction();

    void setValue(@Nullable String value);

    void setValue(@Nullable Integer value);

    void setValue(@Nullable Long value);

    void setValue(@Nullable Double value);

    void setValue(@Nullable Boolean value);

    void setValue(@Nullable LocalDate value);

    void setValue(@Nullable LocalTime value, @Nullable DateTimePattern pattern);

    void setValue(@Nullable LocalDateTime value);

    void setValue(@Nullable ExcelFunction function);

    void clear();

    CellType getType();

    @Override
    default String getReference() {
        return getCellName();
    }
}
