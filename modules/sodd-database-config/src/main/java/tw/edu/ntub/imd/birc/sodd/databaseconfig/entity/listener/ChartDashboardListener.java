package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class ChartDashboardListener {

    @PrePersist
    public void preSave(ChartDashboard chartDashboard) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (chartDashboard.getAvailable() == null) {
            chartDashboard.setAvailable(true);
        }
        if (StringUtils.isBlank(chartDashboard.getCreateId())) {
            chartDashboard.setCreateId(userId);
        }
        if (chartDashboard.getCreateDate() == null) {
            chartDashboard.setCreateDate(now);
        }
        if (chartDashboard.getModifyId() == null) {
            chartDashboard.setModifyId(userId);
        }
        if (chartDashboard.getModifyDate() == null) {
            chartDashboard.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(ChartDashboard chartDashboard) {
        if (chartDashboard.getModifyId() == null) {
            chartDashboard.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (chartDashboard.getModifyDate() == null) {
            chartDashboard.setModifyDate(LocalDateTime.now());
        }
    }
}