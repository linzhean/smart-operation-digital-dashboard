package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class ApplicationListener {
    @PrePersist
    public void preSave(Application application) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (application.getApplyStatus() == null) {
            application.setApplyStatus(Apply.NOT_PASSED);
        }
        if (application.getAvailable() == null) {
            application.setAvailable(true);
        }
        if (StringUtils.isBlank(application.getCreateId())) {
            application.setCreateId(userId);
        }
        if (application.getCreateDate() == null) {
            application.setCreateDate(now);
        }
        if (application.getModifyId() == null) {
            application.setModifyId(userId);
        }
        if (application.getModifyDate() == null) {
            application.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Application application) {
        if (application.getModifyId() == null) {
            application.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (application.getModifyDate() == null) {
            application.setModifyDate(LocalDateTime.now());
        }
    }
}
