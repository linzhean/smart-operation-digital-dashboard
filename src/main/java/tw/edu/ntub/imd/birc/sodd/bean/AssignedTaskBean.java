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
    @Null(message = "交辦事項流水號 - 不得填寫")
    private Integer id;
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @Size(max = 254, message = "交辦事項名稱 - 不得超過{max}個字")
    @NotBlank(message = "交辦事項名稱 - 未填寫")
    private String name;
    private String defaultAuditor;
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

