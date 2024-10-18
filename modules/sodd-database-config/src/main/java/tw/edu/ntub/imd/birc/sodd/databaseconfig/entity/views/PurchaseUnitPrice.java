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
@Table(name = "purchase_unit_price", schema = Config.DATABASE_NAME)
public class PurchaseUnitPrice implements CalJsonToInfo{
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
     * @since 1.0.0
     */
    @Column(name = "date")
    private Timestamp date;
    /**
     * @since 1.0.0
     */
    @Column(name = "purchase_quantity")
    private BigDecimal purchaseQuantity;
    /**
     * @since 1.0.0
     */
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> purchaseQuantitys = entityListData.get("purchaseQuantity");
        List<Object> purchasePrices = entityListData.get("purchasePrice");

        List<Object> purchaseUnitPrices = new ArrayList<>();
        if (purchaseQuantitys.size() == purchasePrices.size()) {
            for (int i = 0; i < purchaseQuantitys.size(); i++) {
                BigDecimal purchaseQuantity = new BigDecimal(purchaseQuantitys.get(i).toString());
                BigDecimal purchasePrice = new BigDecimal(purchasePrices.get(i).toString());

                // 計算公式: purchaseQuantity / purchasePrice
                BigDecimal purchaseUnitPrice = purchaseQuantity.divide(purchasePrice, 2, RoundingMode.HALF_UP);

                purchaseUnitPrices.add(purchaseUnitPrice);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("purchaseUnitPrice", purchaseUnitPrices);
            return newDataMap;
        } else {
            throw new RuntimeException("進貨單價資料解析或計算有誤");
        }
    }
}
