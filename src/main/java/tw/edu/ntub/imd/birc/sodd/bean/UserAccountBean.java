package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import java.time.LocalDateTime;

@Data
public class UserAccountBean {
    private String userId;
    private String userName;
    private String departmentId;
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
