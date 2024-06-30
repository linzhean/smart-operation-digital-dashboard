package tw.edu.ntub.imd.birc.sodd.dto.excel.function;

public interface ExcelFunction extends Reference {
    String getFunctionDefineString();

    boolean isArrayFunction();

    @Override
    default String getReference() {
        return getFunctionDefineString();
    }
}
