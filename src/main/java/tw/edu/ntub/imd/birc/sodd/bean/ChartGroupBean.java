package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChartGroupBean {
    private Integer id;
    private Integer chartId;
    private Integer groupId;
    private Boolean available;
    private String createId;
    private LocalDateTime createDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
