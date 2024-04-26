package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class GroupBean {
    @Null(message = "群組流水號 - 不得填寫")
    private Integer id;
    @Size(max = 254, message = "群組名稱 - 不得超過{max}個字")
    @NotNull(message = "群組名稱 - 未填寫")
    private String name;
    @Null(message = "是否可用 - 不得填寫")
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
