package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class ApplicationBean {
    @Null(message = "申請流水號 - 不得填寫")
    private Integer id;
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @NotBlank(message = "申請人ID - 未填寫")
    private String applicant;
    @NotBlank(message = "保證人ID - 未填寫")
    private String guarantor;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @NotBlank(message = "開始日期 - 未填寫")
    private String startDateStr;
    @NotBlank(message = "結束日期 - 未填寫")
    private String endDateStr;
    @NotBlank(message = "申請原因 - 未填寫")
    private String reason;
    @Null(message = "申請狀態 - 不得填寫")
    private Apply applyStatus;
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
}

