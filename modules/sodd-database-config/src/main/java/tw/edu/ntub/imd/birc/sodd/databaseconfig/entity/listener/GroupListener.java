package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Groups;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class GroupListener {
    @PrePersist
    public void preSave(Groups groups) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (groups.getAvailable() == null) {
            groups.setAvailable(true);
        }
        if (StringUtils.isBlank(groups.getCreateId())) {
            groups.setCreateId(userId);
        }
        if (groups.getCreateDate() == null) {
            groups.setCreateDate(now);
        }
        if (groups.getModifyId() == null) {
            groups.setModifyId(userId);
        }
        if (groups.getModifyDate() == null) {
            groups.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Groups groups) {
        if (groups.getModifyId() == null) {
            groups.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (groups.getModifyDate() == null) {
            groups.setModifyDate(LocalDateTime.now());
        }
    }
}
