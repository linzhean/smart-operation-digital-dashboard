package tw.edu.ntub.imd.birc.sodd.util.file;

import lombok.experimental.UtilityClass;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileExtensionIllegalException;

@UtilityClass
public class FileExtensionUtils {
    public Boolean checkImage(String fileName) {
        String s = FileUtils.getFileExtension(fileName).toLowerCase();
        switch (s) {
            case "png":
            case "jpg":
            case "jpeg":
                return true;
            default:
                throw new FileExtensionIllegalException("請上傳副檔名為jpg或png的圖片");
        }
    }

    public Boolean checkFile(String fileName) {
        String s = FileUtils.getFileExtension(fileName).toLowerCase();
        switch (s) {
            case "pdf":
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
                return true;
            default:
                throw new FileExtensionIllegalException("檔案類型錯誤");
        }
    }
}
