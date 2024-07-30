package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AssignedTasksListener {
    @PrePersist
    public void preSave(AssignedTasks assignedTask) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (assignedTask.getAvailable() == null) {
            assignedTask.setAvailable(true);
        }
        if (StringUtils.isBlank(assignedTask.getCreateId())) {
            assignedTask.setCreateId(userId);
        }
        if (assignedTask.getCreateDate() == null) {
            assignedTask.setCreateDate(now);
        }
        if (assignedTask.getModifyId() == null) {
            assignedTask.setModifyId(userId);
        }
        if (assignedTask.getModifyDate() == null) {
            assignedTask.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(AssignedTasks assignedTask) {
        if (assignedTask.getModifyId() == null) {
            assignedTask.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (assignedTask.getModifyDate() == null) {
            assignedTask.setModifyDate(LocalDateTime.now());
        }
    }
}
