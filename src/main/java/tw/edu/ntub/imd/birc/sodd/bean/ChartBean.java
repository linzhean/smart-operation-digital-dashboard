package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class ChartBean {
    @Null(message = "圖表ID - 不得填寫")
    private Integer id;
    @NotBlank(message = "圖表名稱 - 未填寫")
    private String name;
    @NotNull(message = "生成圖表檔案 - 未上傳")
    private MultipartFile scriptFile;
    @Null(message = "生成圖表檔案路徑 - 不得填寫")
    private String scriptPath;
    @NotNull(message = "圖表縮圖檔案 - 未上傳")
    private MultipartFile imageFile;
    @Null(message = "縮圖檔案路徑 - 不得填寫")
    private String showcaseImage;
    @Null(message = "資料來源(產生圖表的對應資料表) - 不得填寫")
    private String dataSource;
    @Null(message = "靜態圖表 - 不得填寫")
    private String chartImage;
    @Null(message = "互動式圖表檔案 - 不得填寫")
    private Resource chartHTML;
    @Null(message = "是否可發送交辦 - 不得填寫")
    private Boolean canAssign;
    @Null(message = "是否可視 - 不得填寫")
    private Boolean observable;
    @Null(message = "是否啟用 - 不得填寫")
    private Boolean available;
    @Null(message = "創建人ID - 不得填寫")
    private String createId;
    @Null(message = "創建日期 - 不得填寫")
    private LocalDateTime createDate;
    @Null(message = "修改人ID - 不得填寫")
    private String modifyId;
    @Null(message = "修改日期 - 不得填寫")
    private LocalDateTime modifyDate;
    private Integer chartGroupId;
}
