package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

@Data
public class ChartBean {
    private Integer id;
    private String name;
    private String scriptPath;
    private String showcaseImage;
    private Resource chart;
    private Boolean available;
    private String createId;
    private LocalDateTime createDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
