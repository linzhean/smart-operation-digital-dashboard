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
@Table(name = "subcontracting_refund_rate", schema = Config.DATABASE_NAME)
public class SubcontractingRefundRate implements CalJsonToInfo{
    /**
     * 流水號
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * @since 1.0.0
     */
    @Column(name = "product_number")
    private String productNumber;
    /**
      @since 1.0.0
     */
    @Column(name = "date")
    private Timestamp date;
    /**
      @since 1.0.0
     */
    @Column(name = "processed_volume")
    private BigDecimal processedVolume;
    /**
      @since 1.0.0
     */
    @Column(name = "refund_volume")
    private BigDecimal refundVolume;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> processedVolumes = entityListData.get("processedVolume");
        List<Object> refundVolumes = entityListData.get("refundVolume");

        List<Object> subcontractingRefundRates = new ArrayList<>();
        if (processedVolumes.size() == refundVolumes.size()) {
            for (int i = 0; i < processedVolumes.size(); i++) {
                BigDecimal processVolume = new BigDecimal(processedVolumes.get(i).toString());
                BigDecimal refundVolume = new BigDecimal(refundVolumes.get(i).toString());

                // 計算公式: refundVolume / salesVolume * 100
                BigDecimal subcontractingRefundRate = refundVolume.divide(processVolume, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                subcontractingRefundRates.add(subcontractingRefundRate);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("subcontractingRefundRate", subcontractingRefundRates);
            return newDataMap;
        } else {
            throw new RuntimeException("委外加工退貨率資料解析或計算有誤");
        }
    }
}
