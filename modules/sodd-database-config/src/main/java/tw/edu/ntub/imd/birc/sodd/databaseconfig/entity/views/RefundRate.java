package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "refund_rate", schema = Config.DATABASE_NAME)
public class RefundRate implements CalJsonToInfo {
    /**
     * 退貨率ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "product_number")
    private String productNumber;
    @Basic
    @Column(name = "date")
    private Timestamp date;
    @Basic
    @Column(name = "sales_volume")
    private BigDecimal salesVolume;
    @Basic
    @Column(name = "refund_volume")
    private BigDecimal refundVolume;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> refundVolumes = entityListData.get("refundVolume");
        List<Object> salesVolumes = entityListData.get("salesVolume");

        List<Object> refundRates = new ArrayList<>();
        if (refundVolumes.size() == salesVolumes.size()) {
            for (int i = 0; i < refundVolumes.size(); i++) {
                BigDecimal refundVolume = new BigDecimal(refundVolumes.get(i).toString());
                BigDecimal salesVolume = new BigDecimal(salesVolumes.get(i).toString());

                // 計算公式: refundVolume / salesVolume * 100
                BigDecimal refundRate = refundVolume.divide(salesVolume, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                refundRates.add(refundRate);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("refundRate", refundRates);
            return newDataMap;
        } else {
            throw new RuntimeException("退貨率資料解析或計算有誤");
        }
    }
}
