package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.AIGenType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class AiChatBean {
    @Null(message = "流水號 - 不得填寫")
    private Integer id;
    @NotNull(message = "圖表ID - 未填寫")
    private Integer chartId;
    @NotNull(message = "上則訊息ID - 未填寫")
    private Integer messageId;
    @Null(message = "生成方 - 不得填寫")
    private AIGenType generator;
    @NotBlank(message = "交談訊息 - 未填寫")
    private String content;
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
