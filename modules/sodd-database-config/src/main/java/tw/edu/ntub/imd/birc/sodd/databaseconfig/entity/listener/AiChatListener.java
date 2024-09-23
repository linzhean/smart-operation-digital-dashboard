package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AiChat;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AiChatListener {
    @PrePersist
    public void preSave(AiChat aiChat) {
        String userId = SecurityUtils.getLoginUserAccount();
        LocalDateTime now = LocalDateTime.now();
        if (aiChat.getAvailable() == null) {
            aiChat.setAvailable(true);
        }
        if (StringUtils.isBlank(aiChat.getCreateId())) {
            aiChat.setCreateId(userId);
        }
        if (aiChat.getCreateDate() == null) {
            aiChat.setCreateDate(now);
        }
        if (aiChat.getModifyId() == null) {
            aiChat.setModifyId(userId);
        }
        if (aiChat.getModifyDate() == null) {
            aiChat.setModifyDate(now);
        }
    }

    @PreUpdate
    public void preUpdate(AiChat aiChat) {
        if (aiChat.getModifyId() == null) {
            aiChat.setModifyId(SecurityUtils.getLoginUserAccount());
        }
        if (aiChat.getModifyDate() == null) {
            aiChat.setModifyDate(LocalDateTime.now());
        }
    }
}
