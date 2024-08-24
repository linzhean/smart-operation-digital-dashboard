package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class UserAccountBean {
    @NotBlank(message = "使用者ID - 未填寫")
    private String userId;
    @NotBlank(message = "姓名 - 未填寫")
    private String userName;
    @NotBlank(message = "員工編號 - 未填寫")
    private String jobNumber;
    @NotBlank(message = "部門代號 - 未填寫")
    private String departmentId;
    private String departmentName;
    @Null(message = "google Id - 不得填寫")
    private String googleId;
    @Null(message = "gmail - 不得填寫")
    private String gmail;
    @Null(message = "權限 - 不得填寫")
    private Identity identity;
    @NotBlank(message = "職位 - 未填寫")
    private String position;
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
    private Integer userGroupId;
}
