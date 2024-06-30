package tw.edu.ntub.imd.birc.sodd.dto.excel.range;


import tw.edu.ntub.imd.birc.sodd.enumerate.RangeFixType;

class StaticRowOrColumnRange implements Range {
    private RangeFixType type = RangeFixType.NONE;
    private final String rowNumberOrColumnName;

    public StaticRowOrColumnRange(String rowNumberOrColumnName) {
        this.rowNumberOrColumnName = rowNumberOrColumnName;
    }

    @Override
    public String getRange() {
        String formatPattern;
        switch (type) {
            case ALL:
                formatPattern = "$%s:$%s";
                break;
            case COLUMN:
            case ROW:
            case NONE:
            default:
                formatPattern = "%s:%s";
                break;
        }
        return String.format(formatPattern, rowNumberOrColumnName, rowNumberOrColumnName);
    }

    @Override
    public void setFixType(RangeFixType type) {
        this.type = type;
    }
}
