package tw.edu.ntub.imd.birc.sodd.dto.excel.function;

public class Sum implements ExcelFunction {
    private final Reference reference;

    public Sum(Reference reference) {
        this.reference = reference;
    }

    @Override
    public String getFunctionDefineString() {
        return String.format("SUM(%s)", reference.getReference());
    }

    @Override
    public boolean isArrayFunction() {
        return false;
    }
}
