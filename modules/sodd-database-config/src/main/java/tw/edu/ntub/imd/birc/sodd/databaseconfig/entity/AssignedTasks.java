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
     * 交辦事項ID
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
     * 交辦事項名稱
     *
     * @since 1.0.0
     */
    @Column(name = "name", length = 254, nullable = false)
    private String name;
    /**
     * 交辦事項
     *
     * @since 1.0.0
     */
    @Column(name = "default_processor", length = 254, nullable = false)
    private String defaultProcessor;
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
