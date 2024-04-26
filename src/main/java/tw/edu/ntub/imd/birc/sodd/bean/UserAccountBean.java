package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UserAccountBean {
    @Size(max = 254, message = "email - 不可超過{max}個字")
    @NotBlank(message = "email - 未填寫")
    private String userId;
    @Size(max = 45, message = "姓名 - 不可超過{max}個字")
    @NotBlank(message = "姓名 - 未填寫")
    private String userName;
    private String departmentId;
    @Null(message = "google Id - 不得事先填寫")
    private String googleId;
    private String gmail;
    private Identity identity;
    private String position;
    private Boolean available;
    private String createId;
    private LocalDateTime createDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
