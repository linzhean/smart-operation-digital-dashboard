package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener;

import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Request;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class RequestListener {
    @PrePersist
    public void preSave(Request request) {
        if (StringUtils.isBlank(request.getRequestUserId())) {
            request.setRequestUserId(SecurityUtils.getLoginUserAccount());
        }
        if (request.getRequestTime() == null) {
            request.setRequestTime(LocalDateTime.now());
        }
    }
}
