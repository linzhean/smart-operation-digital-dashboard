package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class GroupListener {
    @PrePersist
    public void preSave(Group group) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (group.getAvailable() == null) {
            group.setAvailable(true);
        }
        if (StringUtils.isBlank(group.getCreateId())) {
            group.setCreateId(userId);
        }
        if (group.getCreateDate() == null) {
            group.setCreateDate(now);
        }
        if (group.getModifyId() == null) {
            group.setModifyId(userId);
        }
        if (group.getModifyDate() == null) {
            group.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Group group) {
        if (group.getModifyId() == null) {
            group.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (group.getModifyDate() == null) {
            group.setModifyDate(LocalDateTime.now());
        }
    }
}
