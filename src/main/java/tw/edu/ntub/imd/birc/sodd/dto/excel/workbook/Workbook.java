package tw.edu.ntub.imd.birc.sodd.dto.excel.workbook;


import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.file.File;
import tw.edu.ntub.imd.birc.sodd.enumerate.ExcelType;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileUnknownException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused")
public interface Workbook extends File, AutoCloseable {
    Object getOriginalObject();

    ExcelType getType();

    List<Sheet> getLoadedSheetList();

    Workbook copyNewWorkbook(String newWorkbookName);

    Sheet getSheet(int index);

    Sheet getSheet(String sheetName);

    default Sheet copySheet(@Nonnull String copySource) {
        return copySheet(copySource, null);
    }

    Sheet copySheet(@Nonnull String copySource, @Nullable String newSheetName);

    void moveSheetAfter(String moveTargetName, String anotherSheetName);

    void moveSheetAfter(Sheet moveTarget, Sheet anotherSheet);

    void moveSheetBefore(String moveTargetName, String anotherSheetName);

    void moveSheetBefore(Sheet moveTarget, Sheet anotherSheet);

    default void saveAs(@Nonnull Path savePath) {
        try {
            FileOutputStream savePathOutputStream = new FileOutputStream(savePath.toFile());
            export(savePathOutputStream);
            savePathOutputStream.close();
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    void export(OutputStream outputStream);
}
