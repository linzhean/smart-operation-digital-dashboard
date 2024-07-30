package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AssignedTaskSponsorListener {
    @PrePersist
    public void preSave(AssignedTaskSponsor assignedTaskSponsor) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (assignedTaskSponsor.getAvailable() == null) {
            assignedTaskSponsor.setAvailable(true);
        }
        if (StringUtils.isBlank(assignedTaskSponsor.getCreateId())) {
            assignedTaskSponsor.setCreateId(userId);
        }
        if (assignedTaskSponsor.getCreateDate() == null) {
            assignedTaskSponsor.setCreateDate(now);
        }
        if (assignedTaskSponsor.getModifyId() == null) {
            assignedTaskSponsor.setModifyId(userId);
        }
        if (assignedTaskSponsor.getModifyDate() == null) {
            assignedTaskSponsor.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(AssignedTaskSponsor assignedTaskSponsor) {
        if (assignedTaskSponsor.getModifyId() == null) {
            assignedTaskSponsor.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (assignedTaskSponsor.getModifyDate() == null) {
            assignedTaskSponsor.setModifyDate(LocalDateTime.now());
        }
    }
}
