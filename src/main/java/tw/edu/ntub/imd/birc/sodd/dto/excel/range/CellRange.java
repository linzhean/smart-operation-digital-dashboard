package tw.edu.ntub.imd.birc.sodd.dto.excel.range;

import lombok.Getter;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.enumerate.RangeFixType;


@Getter
public class CellRange implements Range {
    private RangeFixType type = RangeFixType.NONE;
    private final Cell firstCell;
    private final Cell anotherCell;

    public CellRange(Cell firstCell, Cell anotherCell) {
        this.firstCell = firstCell;
        this.anotherCell = anotherCell;
    }

    @Override
    public String getRange() {
        String formatPattern;
        switch (type) {
            case ALL:
                formatPattern = "$%s$%d:$%s$%d";
                break;
            case COLUMN:
                formatPattern = "$%s%d:$%s%d";
                break;
            case ROW:
                formatPattern = "%s$%d:%s$%d";
                break;
            case NONE:
            default:
                formatPattern = "%s%d:%s%d";
        }
        return String.format(
                formatPattern,
                firstCell.getColumnName(),
                firstCell.getRowNumber(),
                anotherCell.getColumnName(),
                anotherCell.getRowNumber()
        );
    }

    @Override
    public void setFixType(RangeFixType type) {
        this.type = type;
    }
}
