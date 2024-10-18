package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class AssignedTaskBean {
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @NotBlank(message = "預設稽核人 - 未填寫")
    private String defaultAuditor;
    @NotBlank(message = "預設處理人 - 未填寫")
    private String defaultProcessor;
    private Double upperLimit;
    private Double lowerLimit;
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

