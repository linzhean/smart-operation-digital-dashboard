package tw.edu.ntub.imd.birc.sodd.util.http;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileUnknownException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class ExcelResponseUtils {

    public void response(HttpServletResponse response, Workbook workbook) {
        try {
            String fileName = workbook.getFullFileName();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
            workbook.export(response.getOutputStream());
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }
}
