package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.RequestType;

import java.time.LocalDateTime;

@Data
public class RequestBean {
    private Integer requestId;
    private RequestType mapping;
    private String userAgent;
    private String url;
    private String filePath;
    private String message;
    private String requestIpFrom;
    private Identity identity;
    private String requestUserId;
    private LocalDateTime requestTime;
}