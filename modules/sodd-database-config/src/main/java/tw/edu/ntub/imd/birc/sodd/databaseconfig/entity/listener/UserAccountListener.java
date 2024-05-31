package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class UserAccountListener {
    @PrePersist
    public void preSave(UserAccount userAccount) {
        String userId = "admin";
        LocalDateTime now = LocalDateTime.now();
        if (userAccount.getIdentity() == null) {
            userAccount.setIdentity(Identity.NO_PERMISSION);
        }
        if (userAccount.getAvailable() == null) {
            userAccount.setAvailable(true);
        }
        if (StringUtils.isBlank(userAccount.getCreateId())) {
            userAccount.setCreateId(userId);
        }
        if (userAccount.getCreateDate() == null) {
            userAccount.setCreateDate(now);
        }
        if (userAccount.getModifyId() == null) {
            userAccount.setModifyId(userId);
        }
        if (userAccount.getModifyDate() == null) {
            userAccount.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(UserAccount userAccount) {
        if (userAccount.getModifyId() == null) {
            userAccount.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (userAccount.getModifyDate() == null) {
            userAccount.setModifyDate(LocalDateTime.now());
        }
    }
}
