package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class ChartGroupListener {
    @PrePersist
    public void preSave(ChartGroup chartGroup) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (chartGroup.getAvailable() == null) {
            chartGroup.setAvailable(true);
        }
        if (StringUtils.isBlank(chartGroup.getCreateId())) {
            chartGroup.setCreateId(userId);
        }
        if (chartGroup.getCreateDate() == null) {
            chartGroup.setCreateDate(now);
        }
        if (chartGroup.getModifyId() == null) {
            chartGroup.setModifyId(userId);
        }
        if (chartGroup.getModifyDate() == null) {
            chartGroup.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(ChartGroup chartGroup) {
        if (chartGroup.getModifyId() == null) {
            chartGroup.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (chartGroup.getModifyDate() == null) {
            chartGroup.setModifyDate(LocalDateTime.now());
        }
    }
}
