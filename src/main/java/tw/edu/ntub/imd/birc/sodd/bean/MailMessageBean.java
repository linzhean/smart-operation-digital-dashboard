package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class MailMessageBean {
    @Null(message = "流水號 - 不得填寫")
    private Integer id;
    @NotNull(message = "郵件ID - 未填寫")
    private Integer mailId;
    @Null(message = "上則訊息ID - 不得填寫")
    private Integer messageId;
    @NotBlank(message = "郵件內容 - 未填寫")
    private String content;
    private String available;
    private String createId;
    private LocalDateTime createDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
