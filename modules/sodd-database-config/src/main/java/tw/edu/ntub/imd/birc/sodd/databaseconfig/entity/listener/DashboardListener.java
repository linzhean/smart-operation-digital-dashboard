package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class DashboardListener {
    @PrePersist
    public void preSave(Dashboard dashboard) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (dashboard.getAvailable() == null) {
            dashboard.setAvailable(true);
        }
        if (StringUtils.isBlank(dashboard.getCreateId())) {
            dashboard.setCreateId(userId);
        }
        if (dashboard.getCreateDate() == null) {
            dashboard.setCreateDate(now);
        }
        if (dashboard.getModifyId() == null) {
            dashboard.setModifyId(userId);
        }
        if (dashboard.getModifyDate() == null) {
            dashboard.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Dashboard dashboard) {
        if (dashboard.getModifyId() == null) {
            dashboard.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (dashboard.getModifyDate() == null) {
            dashboard.setModifyDate(LocalDateTime.now());
        }
    }
}
