package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class AssignedTaskSponsorBean {
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @NotNull(message = "發起人ID - 未填寫")
    private String sponsorUserId;
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
