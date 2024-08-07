package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
