package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class ChartGroupId implements Serializable {
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 群組ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
}
