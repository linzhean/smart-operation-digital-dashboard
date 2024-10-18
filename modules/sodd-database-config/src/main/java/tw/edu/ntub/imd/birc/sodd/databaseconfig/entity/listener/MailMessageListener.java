package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.MailMessage;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class MailMessageListener {
    @PrePersist
    public void preSave(MailMessage mailMessage) {
        LocalDateTime now = LocalDateTime.now();
        if (mailMessage.getAvailable() == null) {
            mailMessage.setAvailable(true);
        }
        if (StringUtils.isBlank(mailMessage.getCreateId())) {
            mailMessage.setCreateId(SecurityUtils.getLoginUserAccount());
        }
        if (mailMessage.getCreateDate() == null) {
            mailMessage.setCreateDate(now);
        }
        if (StringUtils.isBlank(mailMessage.getModifyId())) {
            mailMessage.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (mailMessage.getModifyDate() == null) {
            mailMessage.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(MailMessage mailMessage) {
        if (StringUtils.isBlank(mailMessage.getModifyId())) {
            mailMessage.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (mailMessage.getModifyDate() == null) {
            mailMessage.setModifyDate(LocalDateTime.now());
        }
    }
}
