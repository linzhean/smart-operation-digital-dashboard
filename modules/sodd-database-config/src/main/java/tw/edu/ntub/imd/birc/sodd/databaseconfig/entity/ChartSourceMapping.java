package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.util.Objects;

/**
 * 圖表資料來源與原始資料來源對應
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "chart_source_mapping", schema = Config.DATABASE_NAME)
public class ChartSourceMapping {
    /**
     * 資料來源(產生圖表的對應資料表)
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "data_source")
    private String dataSource;
    /**
     * 原始資料來源
     *
     * @since 1.0.0
     */
    @Column(name = "original_source")
    private String originalSource;
}
