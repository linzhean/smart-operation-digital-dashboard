package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

/**
 * 離職率
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "turnover_rate", schema = Config.DATABASE_NAME)
public class TurnoverRate implements CalJsonToInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /**
     * @since 1.0.0
     */
    @Column(name = "date")
    private Timestamp date;
    /**
     * @since 1.0.0
     */
    @Column(name = "department_name")
    private String departmentName;
    /**
     * @since 1.0.0
     */
    @Column(name = "initial_headcount")
    private BigDecimal initialHeadcount;
    /**
     * @since 1.0.0
     */
    @Column(name = "resigned_headcount")
    private BigDecimal resignedHeadcount;
    /**
     * @since 1.0.0
     */
    @Column(name = "ending_headcount")
    private BigDecimal endingHeadcount;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> initialHeadcounts = entityListData.get("initialHeadcount");
        List<Object> resignedHeadcounts = entityListData.get("resignedHeadcount");
        List<Object> endingHeadcounts = entityListData.get("endingHeadcount");

        List<Object> turnoverRates = new ArrayList<>();
        if (initialHeadcounts.size() == resignedHeadcounts.size() &&
                resignedHeadcounts.size() == endingHeadcounts.size()) {
            for (int i = 0; i < initialHeadcounts.size(); i++) {
                BigDecimal initialHeadcount = new BigDecimal(initialHeadcounts.get(i).toString());
                BigDecimal resignedHeadcount = new BigDecimal(resignedHeadcounts.get(i).toString());
                BigDecimal endingHeadcount = new BigDecimal(endingHeadcounts.get(i).toString());

                // 計算 (initialHeadcount + endingHeadcount) / 2
                BigDecimal averageHeadcount = initialHeadcount.add(endingHeadcount).divide(new BigDecimal(2), RoundingMode.HALF_UP);

                // 計算 resignedHeadcount / averageHeadcount
                BigDecimal turnoverRate = resignedHeadcount.divide(averageHeadcount, 5, RoundingMode.HALF_UP);

                // 乘以 100 得到百分比
                turnoverRate = turnoverRate.multiply(new BigDecimal(100));

                turnoverRates.add(turnoverRate);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("turnoverRate", turnoverRates);
            return newDataMap;
        } else {
            throw new RuntimeException("離職率資料解析或計算有誤");
        }
    }
}
