package tw.edu.ntub.imd.birc.sodd.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SyncRecordBean {
    private Integer id;
    private LocalDateTime syncTime;
}
