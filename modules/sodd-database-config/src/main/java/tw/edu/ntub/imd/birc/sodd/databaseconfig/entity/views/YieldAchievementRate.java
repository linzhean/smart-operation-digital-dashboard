package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 產量達成率
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "yield_achievement_rate", schema = Config.DATABASE_NAME)
public class YieldAchievementRate {
    /**
     * 產量達成率ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /**
     * 品號
     *
     * @since 1.0.0
     */
    @Column(name = "product_number")
    private String productNumber;
    /**
     * 日期
     *
     * @since 1.0.0
     */
    @Column(name = "date")
    private LocalDateTime date;
    /**
     * 預計產量
     *
     * @since 1.0.0
     */
    @Column(name = "expected_output")
    private BigDecimal expectedOutput;
    /**
     * 已生產量
     *
     * @since 1.0.0
     */
    @Column(name = "production_volume")
    private BigDecimal productionVolume;
    /**
     * 提前生產量
     *
     * @since 1.0.0
     */
    @Column(name = "advance_quantity")
    private BigDecimal advanceQuantity;
}
