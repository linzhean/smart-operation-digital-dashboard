package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class UserGroupListener {
    @PrePersist
    public void preSave(UserGroup userGroup) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (userGroup.getAvailable() == null) {
            userGroup.setAvailable(true);
        }
        if (StringUtils.isBlank(userGroup.getCreateId())) {
            userGroup.setCreateId(userId);
        }
        if (userGroup.getCreateDate() == null) {
            userGroup.setCreateDate(now);
        }
        if (userGroup.getModifyId() == null) {
            userGroup.setModifyId(userId);
        }
        if (userGroup.getModifyDate() == null) {
            userGroup.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(UserGroup userGroup) {
        if (userGroup.getModifyId() == null) {
            userGroup.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (userGroup.getModifyDate() == null) {
            userGroup.setModifyDate(LocalDateTime.now());
        }
    }
}
