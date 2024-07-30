package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Export;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class ExportListener {
    @PrePersist
    public void preSave(Export export) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (export.getAvailable() == null) {
            export.setAvailable(true);
        }
        if (StringUtils.isBlank(export.getCreateId())) {
            export.setCreateId(userId);
        }
        if (export.getCreateDate() == null) {
            export.setCreateDate(now);
        }
        if (export.getModifyId() == null) {
            export.setModifyId(userId);
        }
        if (export.getModifyDate() == null) {
            export.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Export export) {
        if (export.getModifyId() == null) {
            export.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (export.getModifyDate() == null) {
            export.setModifyDate(LocalDateTime.now());
        }
    }
}
