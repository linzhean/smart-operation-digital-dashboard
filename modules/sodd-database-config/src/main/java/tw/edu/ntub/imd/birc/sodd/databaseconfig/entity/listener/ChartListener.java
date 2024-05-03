package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class ChartListener {
    @PrePersist
    public void preSave(Chart chart) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (chart.getAvailable() == null) {
            chart.setAvailable(true);
        }
        if (StringUtils.isBlank(chart.getCreateId())) {
            chart.setCreateId(userId);
        }
        if (chart.getCreateDate() == null) {
            chart.setCreateDate(now);
        }
        if (chart.getModifyId() == null) {
            chart.setModifyId(userId);
        }
        if (chart.getModifyDate() == null) {
            chart.setModifyDate(now);
        }
    }
}
