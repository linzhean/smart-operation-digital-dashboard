package tw.edu.ntub.imd.birc.sodd.enumerate;

import lombok.Getter;
import tw.edu.ntub.imd.birc.sodd.util.file.FileUtils;

import java.nio.file.Path;

public enum ExcelType {
    XLS("xls"), XLSX("xlsx"), XLSM("xlsm");

    @Getter
    private final String extension;

    ExcelType(String extension) {
        this.extension = extension;
    }

    public static ExcelType getByExcelPath(Path workbookPath) {
        Path fileNamePath = workbookPath.getFileName();
        String fileName = fileNamePath.toString();
        String fileExtension = FileUtils.getFileExtension(fileName);
        return getByFileExtension(fileExtension);
    }

    public static ExcelType getByFileExtension(String extension) {
        for (ExcelType excelType : values()) {
            if (excelType.extension.equals(extension)) {
                return excelType;
            }
        }
        throw new IllegalArgumentException("無此Excel類型");
    }
}
