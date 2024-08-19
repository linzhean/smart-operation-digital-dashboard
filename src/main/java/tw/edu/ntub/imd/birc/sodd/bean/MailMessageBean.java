package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class MailMessageBean {
    @Null(message = "訊息流水號 - 不得填寫")
    private Integer id;
    @Null(message = "郵件ID - 不得填寫")
    private Integer mailId;
    @NotNull(message = "訊息ID - 未填寫")
    private Integer messageId;
    @NotBlank(message = "郵件內容 - 未填寫")
    private String content;
    @Null(message = "是否啟用 - 不得填寫")
    private String available;
    @Null(message = "創建人ID - 不得填寫")
    private String createId;
    @Null(message = "創建日期 - 不得填寫")
    private LocalDateTime createDate;
    @Null(message = "修改人ID - 不得填寫")
    private String modifyId;
    @Null(message = "修改日期 - 不得填寫")
    private LocalDateTime modifyDate;
}
