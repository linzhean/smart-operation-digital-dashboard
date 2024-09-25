package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class MailListener {
    @PrePersist
    public void preSave(Mail mail) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.isBlank(mail.getPublisher())) {
            mail.setPublisher(userId);
        }
        if (mail.getEmailSendTime() == null) {
            mail.setEmailSendTime(now);
        }
        if (mail.getAvailable() == null) {
            mail.setAvailable(true);
        }
        if (StringUtils.isBlank(mail.getCreateId())) {
            mail.setCreateId(userId);
        }
        if (mail.getCreateDate() == null) {
            mail.setCreateDate(now);
        }
        if (StringUtils.isBlank(mail.getModifyId())) {
            mail.setModifyId(userId);
        }
        if (mail.getModifyDate() == null) {
            mail.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(Mail mail) {
        if (StringUtils.isBlank(mail.getModifyId())) {
            mail.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (mail.getModifyDate() == null) {
            mail.setModifyDate(LocalDateTime.now());
        }
    }
}
