package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.AssignedTasksListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 交辦事項
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(AssignedTasksListener.class)
@Table(name = "assigned_tasks", schema = Config.DATABASE_NAME)
public class AssignedTasks {
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 預設稽核者
     *
     * @since 1.0.0
     */
    @Column(name = "default_auditor", length = 254, nullable = false)
    private String defaultAuditor;
    /**
     * 預設處理人
     *
     * @since 1.0.0
     */
    @Column(name = "default_processor", length = 254, nullable = false)
    private String defaultProcessor;
    /**
     * 上限
     *
     * @since 1.0.0
     */
    @Column(name = "upper_limit")
    private Double upperLimit;
    /**
     * 下限
     *
     * @since 1.0.0
     */
    @Column(name = "lower_limit")
    private Double lowerLimit;
    /**
     * 是否啟用(0: 不啟用，1: 啟用)
     *
     * @since 1.0.0
     */
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "available", nullable = false)
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
