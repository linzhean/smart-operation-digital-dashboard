package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Data
@Entity
@Table(name = "inventory_levels", schema = Config.DATABASE_NAME)
public class InventoryLevels implements CalJsonToInfo{
/**
 * 流水號
 *
 * @since 1.0.0
 */
@Id
@Column(name = "id")
private String id;
    /**
     * @since 1.0.0
     */
    @Column(name = "product_number")
    private String productNumber;
    /**
     * @since 1.0.0
     */
    @Column(name = "date")
    private Timestamp date;
    /**
     * @since 1.0.0
     */
    @Column(name = "gross_demand")
    private BigDecimal grossDemand;
    /**
     * @since 1.0.0
     */
    @Column(name = "stocks")
    private BigDecimal stocks;
    /**
     * 服務水準係數 Z
     */
    private static final BigDecimal SERVICE_LEVEL_COEFFICIENT = new BigDecimal("1.65"); // 可調整 Z 值

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> grossDemandList = entityListData.get("grossDemand");
        List<Object> stocksList = entityListData.get("stocks");
        List<Object> inventoryRateList = new ArrayList<>();

        if (grossDemandList != null && stocksList != null) {
            for (int i = 0; i < grossDemandList.size(); i++) {
                BigDecimal currentStock = new BigDecimal(stocksList.get(i).toString());
                BigDecimal demandStdDev = new BigDecimal(grossDemandList.get(i).toString()); // 假設為標準差

                // 計算安全庫存量 = Z × σ 需求量或提前期
                BigDecimal safetyStock = SERVICE_LEVEL_COEFFICIENT.multiply(demandStdDev);

                // 計算當前庫存量 ÷ 安全庫存量 × 100%
                BigDecimal inventoryRate = currentStock
                        .divide(safetyStock, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                inventoryRateList.add(inventoryRate);
            }
        }

        // 新增計算結果到返回的資料中
        entityListData.put("inventoryRate", inventoryRateList);
        return entityListData;
    }
}