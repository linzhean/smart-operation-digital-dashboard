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
@Table(name = "cost_variance_rate", schema = Config.DATABASE_NAME)
public class CostVarianceRate implements CalJsonToInfo{
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
    @Column(name = "actual_labor_cost")
    private BigDecimal actualLaborCost;
    /**
     * @since 1.0.0
     */
    @Column(name = "actual_material_cost")
    private BigDecimal actualMaterialCost;
    /**
     * @since 1.0.0
     */
    @Column(name = "actual_manufacturing_expense")
    private BigDecimal actualManufacturingExpense;
    /**
     * @since 1.0.0
     */
    @Column(name = "actual_processing_cost")
    private BigDecimal actualProcessingCost;
    /**
     * @since 1.0.0
     */
    @Column(name = "standard_labor_cost")
    private BigDecimal standardLaborCost;
    /**
     * @since 1.0.0
     */
    @Column(name = "standard_material_cost")
    private BigDecimal standardMaterialCost;
    /**
     * @since 1.0.0
     */
    @Column(name = "standard_manufacturing_expense")
    private BigDecimal standardManufacturingExpense;
    /**
     * @since 1.0.0
     */
    @Column(name = "standard_processing_cost")
    private BigDecimal standardProcessingCost;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> actualLaborCosts = entityListData.get("actualLaborCost");
        List<Object> actualMaterialCosts = entityListData.get("actualMaterialCost");
        List<Object> actualManufacturingExpenses = entityListData.get("actualManufacturingExpense");
        List<Object> actualProcessingCosts = entityListData.get("actualProcessingCost");
        List<Object> standardLaborCosts = entityListData.get("standardLaborCost");
        List<Object> standardMaterialCosts = entityListData.get("standardMaterialCost");
        List<Object> standardManufacturingExpenses = entityListData.get("standardManufacturingExpense");
        List<Object> standardProcessingCosts = entityListData.get("standardProcessingCost");

        List<Object> costVarianceRates = new ArrayList<>();
        if (actualLaborCosts.size() == actualMaterialCosts.size() &&
                actualMaterialCosts.size() == actualManufacturingExpenses.size() &&
                actualManufacturingExpenses.size() == actualProcessingCosts.size() &&
                actualProcessingCosts.size() == standardLaborCosts.size() &&
                standardLaborCosts.size() == standardMaterialCosts.size() &&
                standardMaterialCosts.size() == standardManufacturingExpenses.size() &&
                standardManufacturingExpenses.size() == standardProcessingCosts.size()) {
            for (int i = 0; i < actualLaborCosts.size(); i++) {
                BigDecimal actualLaborCost = new BigDecimal(actualLaborCosts.get(i).toString());
                BigDecimal actualMaterialCost = new BigDecimal(actualMaterialCosts.get(i).toString());
                BigDecimal actualManufacturingExpense = new BigDecimal(actualManufacturingExpenses.get(i).toString());
                BigDecimal actualProcessingCost = new BigDecimal(actualProcessingCosts.get(i).toString());
                BigDecimal standardLaborCost = new BigDecimal(standardLaborCosts.get(i).toString());
                BigDecimal standardMaterialCost = new BigDecimal(standardMaterialCosts.get(i).toString());
                BigDecimal standardManufacturingExpense = new BigDecimal(standardManufacturingExpenses.get(i).toString());
                BigDecimal standardProcessingCost = new BigDecimal(standardProcessingCosts.get(i).toString());

                // 計算實際總成本
                BigDecimal actualTotalCost = actualLaborCost.add(actualMaterialCost)
                        .add(actualManufacturingExpense)
                        .add(actualProcessingCost);

                // 計算標準總成本
                BigDecimal standardTotalCost = standardLaborCost.add(standardMaterialCost)
                        .add(standardManufacturingExpense)
                        .add(standardProcessingCost);

                // (實際總成本 - 標準總成本) / 標準總成本 * 100
                BigDecimal costVarianceRate = actualTotalCost.subtract(standardTotalCost)
                        .divide(standardTotalCost, 4, RoundingMode.HALF_UP) // 精確到4位小數
                        .multiply(BigDecimal.valueOf(100));

                costVarianceRates.add(costVarianceRate);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("costVarianceRate", costVarianceRates);
            return newDataMap;
        } else {
            throw new RuntimeException("生產成本偏差率資料解析或計算有誤");
        }
    }
}
