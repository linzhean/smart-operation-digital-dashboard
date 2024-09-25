package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.ChartListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 圖表
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(ChartListener.class)
@Table(name = "chart", schema = Config.DATABASE_NAME)
public class Chart {
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 圖表名稱
     *
     * @since 1.0.0
     */
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    /**
     * 產生圖表的檔案路徑
     *
     * @since 1.0.0
     */
    @Column(name = "script_path", nullable = false)
    private String scriptPath;
    /**
     * 展示圖
     *
     * @since 1.0.0
     */
    @Column(name = "showcase_image", length = 254, nullable = false)
    private String showcaseImage;
    /**
     * 資料來源(產生圖表的對應資料表)
     *
     * @since 1.0.0
     */
    @Column(name = "data_source", length = 254, nullable = false)
    private String dataSource;
    /**
     * 是否啟用(0: 不啟用 1:啟用)
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
