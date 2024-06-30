package tw.edu.ntub.imd.birc.sodd.dto.excel.workbook;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tw.edu.ntub.imd.birc.sodd.dto.excel.cell.Cell;
import tw.edu.ntub.imd.birc.sodd.dto.excel.function.ExcelFunctionValue;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.PoiSheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.enumerate.ExcelType;
import tw.edu.ntub.imd.birc.sodd.exception.excel.SheetNameExistException;
import tw.edu.ntub.imd.birc.sodd.exception.excel.SheetNotFoundException;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileUnknownException;
import tw.edu.ntub.imd.birc.sodd.util.file.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("unused")
public class PoiWorkbook implements Workbook {
    private final ExcelType type;
    private Path path;
    private String name;
    private final org.apache.poi.ss.usermodel.Workbook workbook;
    private final FormulaEvaluator formulaEvaluator;
    private final List<Sheet> sheetList = new ArrayList<>();

    public PoiWorkbook(@Nonnull Path workbookPath) {
        try {
            this.type = ExcelType.getByExcelPath(workbookPath);
            this.path = workbookPath;
            String fullFileName = FileUtils.getFullFileNameFromPath(workbookPath);
            this.name = FileUtils.getFileName(fullFileName);
            this.workbook = getSize() > 0 ? WorkbookFactory.create(workbookPath.toFile()) : createPoiWorkbook(type);
            CreationHelper creationHelper = workbook.getCreationHelper();
            this.formulaEvaluator = creationHelper.createFormulaEvaluator();
            for (org.apache.poi.ss.usermodel.Sheet sheet : workbook) {
                sheetList.add(new PoiSheet(this, sheet, formulaEvaluator));
            }
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    public PoiWorkbook(@Nonnull ExcelType type, @Nullable String workbookName) {
        this.type = type;
        this.name = workbookName;
        this.workbook = createPoiWorkbook(type);
        CreationHelper creationHelper = workbook.getCreationHelper();
        this.formulaEvaluator = creationHelper.createFormulaEvaluator();
    }

    private org.apache.poi.ss.usermodel.Workbook createPoiWorkbook(@Nonnull ExcelType type) {
        return type == ExcelType.XLS ? new HSSFWorkbook() : new XSSFWorkbook();
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getExtension() {
        return type.getExtension();
    }

    @Nullable
    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public org.apache.poi.ss.usermodel.Workbook getOriginalObject() {
        return workbook;
    }

    @Override
    public ExcelType getType() {
        return type;
    }

    @Override
    public List<Sheet> getLoadedSheetList() {
        return Collections.unmodifiableList(sheetList);
    }

    @Override
    public Workbook copyNewWorkbook(String newWorkbookName) {
        Workbook workbook = new PoiWorkbook(type, newWorkbookName);
        workbook.setPath(path);
        for (Sheet sheet : getLoadedSheetList()) {
            Sheet copySheet = workbook.getSheet(sheet.getName());
            for (Row row : sheet.getLoadedRowList()) {
                Row copyRow = copySheet.getRowByRowNumber(row.getNumber());
                for (Cell cell : row.getLoadedCellList()) {
                    Cell copyCell = copyRow.getCell(cell.getColumnIndex());
                    switch (cell.getType()) {
                        case STRING:
                            copyCell.setValue(cell.getValueAsString());
                            break;
                        case INT:
                            copyCell.setValue(cell.getValueAsInt());
                            break;
                        case DOUBLE:
                            copyCell.setValue(cell.getValueAsDouble());
                            break;
                        case BOOLEAN:
                            copyCell.setValue(cell.getValueAsBoolean());
                            break;
                        case FUNCTION:
                        case ERROR:
                            String dataEditorValue = cell.getDataEditorValue();
                            copyCell.setValue(new ExcelFunctionValue(dataEditorValue));
                            break;
                    }
                }
            }
        }
        return workbook;
    }

    @Override
    public Sheet getSheet(int index) {
        int checkIndex = Objects.checkIndex(index, sheetList.size());
        return sheetList.parallelStream()
                .filter(poiSheet -> poiSheet.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new SheetNotFoundException(index));
    }

    @Override
    public Sheet getSheet(String sheetName) {
        Optional<Sheet> optionalSheet = findSheet(sheetName);
        if (optionalSheet.isPresent()) {
            return optionalSheet.get();
        } else {
            PoiSheet newSheet = new PoiSheet(this, workbook.createSheet(sheetName), formulaEvaluator);
            sheetList.add(newSheet);
            return newSheet;
        }
    }

    @Override
    public Sheet copySheet(@Nonnull String copySource, @Nullable String newSheetName) {
        Optional<Sheet> optionalSheet = findSheet(copySource);
        if (optionalSheet.isPresent()) {
            Sheet sheet = optionalSheet.get();
            org.apache.poi.ss.usermodel.Sheet cloneSheet = workbook.cloneSheet(sheet.getIndex());
            if (newSheetName != null) {
                Optional<Sheet> sheetOptional = findSheet(newSheetName);
                if (sheetOptional.isPresent()) {
                    throw new SheetNameExistException(newSheetName);
                } else {
                    workbook.setSheetName(workbook.getSheetIndex(cloneSheet), newSheetName);
                }
            }
            Sheet newSheet = new PoiSheet(this, cloneSheet, formulaEvaluator);
            sheetList.add(newSheet);
            return newSheet;
        } else {
            return getSheet(newSheetName);
        }
    }

    private Optional<Sheet> findSheet(String sheetName) {
        return sheetList.parallelStream()
                .filter(sheet -> sheet.isNameEquals(sheetName))
                .findFirst();
    }

    @Override
    public void moveSheetAfter(String moveTargetName, String anotherSheetName) {
        Optional<Sheet> optionalMoveTarget = findSheet(moveTargetName);
        Optional<Sheet> optionalAnotherSheet = findSheet(anotherSheetName);
        moveExistSheetAfter(
                optionalMoveTarget.orElseThrow(() -> new SheetNotFoundException(moveTargetName)),
                optionalAnotherSheet.orElseThrow(() -> new SheetNotFoundException(anotherSheetName))
        );
    }

    private void moveExistSheetAfter(Sheet moveTarget, Sheet anotherSheet) {
        int moveTargetIndex = moveTarget.getIndex();
        int anotherSheetIndex = anotherSheet.getIndex();
        if (moveTargetIndex > anotherSheetIndex) {
            workbook.setSheetOrder(moveTarget.getName(), anotherSheetIndex + 1);
        } else if (moveTargetIndex < anotherSheetIndex) {
            workbook.setSheetOrder(moveTarget.getName(), anotherSheetIndex);
        }
    }

    @Override
    public void moveSheetAfter(Sheet moveTarget, Sheet anotherSheet) {
        moveSheetAfter(moveTarget.getName(), anotherSheet.getName());
    }

    @Override
    public void moveSheetBefore(String moveTargetName, String anotherSheetName) {
        Optional<Sheet> optionalMoveTarget = findSheet(moveTargetName);
        Optional<Sheet> optionalAnotherSheet = findSheet(anotherSheetName);
        moveExistSheetBefore(
                optionalMoveTarget.orElseThrow(() -> new SheetNotFoundException(moveTargetName)),
                optionalAnotherSheet.orElseThrow(() -> new SheetNotFoundException(anotherSheetName))
        );
    }

    private void moveExistSheetBefore(Sheet moveTarget, Sheet anotherSheet) {
        int moveTargetIndex = moveTarget.getIndex();
        int anotherSheetIndex = anotherSheet.getIndex();
        if (moveTargetIndex > anotherSheetIndex) {
            workbook.setSheetOrder(moveTarget.getName(), anotherSheetIndex);
        } else if (moveTargetIndex < anotherSheetIndex) {
            workbook.setSheetOrder(moveTarget.getName(), anotherSheetIndex - 1);
        }
    }

    @Override
    public void moveSheetBefore(Sheet moveTarget, Sheet anotherSheet) {
        moveSheetBefore(moveTarget.getName(), anotherSheet.getName());
    }

    @Override
    public void close() throws Exception {
        workbook.close();
    }

    @Override
    public void export(OutputStream outputStream) {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    @Override
    public String toString() {
        return getFullFileName();
    }
}
