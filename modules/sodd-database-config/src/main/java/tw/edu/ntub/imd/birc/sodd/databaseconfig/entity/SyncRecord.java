package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 資料庫同步紀錄
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "sync_record", schema = Config.DATABASE_NAME)
public class SyncRecord {
    /**
     * 資料庫同步紀錄ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 資料庫同步時間
     *
     * @since 1.0.0
     */
    @Column(name = "sync_time", nullable = false)
    private LocalDateTime syncTime;
}
