package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardBean {
    private Integer id;
    private String name;
    private Boolean available;
    private String createId;
    private LocalDateTime createDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
