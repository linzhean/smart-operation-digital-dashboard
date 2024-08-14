package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.ChartDashboardListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 圖表 - 儀表板
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(ChartDashboardListener.class)
@Table(name = "chart_dashboard", schema = Config.DATABASE_NAME)
public class ChartDashboard {
    /**
     * 圖表 - 儀表板ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 儀表板ID
     *
     * @since 1.0.0
     */
    @Column(name = "dashboard_id", nullable = false)
    private Integer dashboardId;
    /**
     * 是否啟用(0: 不啟用 1:啟用)
     *
     * @since 1.0.0
     */
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "available", length = 1, nullable = false)
    private Boolean available;
    /**
     * 創建人ID
     *
     * @since 1.0.0
     */
    @Column(name = "create_id", length = 254, nullable = false)
    private String createId;
    /**
     * 創建日期
     *
     * @since 1.0.0
     */
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    /**
     * 修改人ID
     *
     * @since 1.0.0
     */
    @Column(name = "modify_id", length = 254, nullable = false)
    private String modifyId;
    /**
     * 修改日期
     *
     * @since 1.0.0
     */
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;
}
