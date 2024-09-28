package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 產量達成率
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "yield_achievement_rate", schema = Config.DATABASE_NAME)
public class YieldAchievementRate implements CalJsonToInfo {
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

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> expectedOutputs = entityListData.get("expectedOutput");
        List<Object> productionVolumes = entityListData.get("productionVolume");
        List<Object> advanceQuantities = entityListData.get("advanceQuantity");

        List<Object> yieldAchievementRates = new ArrayList<>();
        if (expectedOutputs.size() == productionVolumes.size() &&
                productionVolumes.size() == advanceQuantities.size()) {
            for (int i = 0; i < expectedOutputs.size(); i++) {
                BigDecimal expectedOutput = new BigDecimal(expectedOutputs.get(i).toString());
                BigDecimal productionVolume = new BigDecimal(productionVolumes.get(i).toString());
                BigDecimal advanceQuantity = new BigDecimal(advanceQuantities.get(i).toString());

                // 計算公式: (productionVolumes + expectedOutputs) / productNumbers * 100
                BigDecimal yieldAchievementRate = (productionVolume.add(advanceQuantity))
                        .divide(expectedOutput, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                yieldAchievementRates.add(yieldAchievementRate);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("yieldAchievementRate", yieldAchievementRates);
            return newDataMap;
        } else {
            throw new RuntimeException("產量達成率資料解析或計算有誤");
        }
    }
}
