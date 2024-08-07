package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MailBean {
    @Null(message = "流水號 - 不得填寫")
    private Integer id;
    @Null(message = "交辦事項ID - 不得填寫")
    private Integer assignedTaskId;
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @NotBlank(message = "郵件名稱 - 未填寫")
    private String name;
    @Null(message = "處理狀態 - 不得填寫")
    private ProcessStatus status;
    @Null(message = "郵件發送人 - 不得填寫")
    private String publisher;
    @NotBlank(message = "郵件接收人 - 未填寫")
    private String receiver;
    @Null(message = "郵件發送時間 - 不得填寫")
    private LocalDateTime emailSendTime;
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
    @Valid
    private MailMessageBean firstMessage;
    @Null(message = "郵件訊息列表 - 不得填寫")
    private List<MailMessageBean> messageList;
}
