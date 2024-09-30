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
@Table(name = "time_efficiency", schema = Config.DATABASE_NAME)
public class TimeEfficiency implements CalJsonToInfo {
    /**
     * 工時效率ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "production_line_name")
    private String productionLineName;
    @Basic
    @Column(name = "date")
    private Timestamp date;
    @Basic
    @Column(name = "total_valid_hours")
    private BigDecimal totalValidHours;
    @Basic
    @Column(name = "total_hours_invested")
    private BigDecimal totalHoursInvested;

    @Override
    public Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData) {
        List<Object> totalValidHours = entityListData.get("totalValidHours");
        List<Object> totalHoursInvesteds = entityListData.get("totalHoursInvested");

        List<Object> timeEfficiencys = new ArrayList<>();
        if (totalValidHours.size() == totalHoursInvesteds.size()) {
            for (int i = 0; i < totalValidHours.size(); i++) {
                BigDecimal totalValidHour = new BigDecimal(totalValidHours.get(i).toString());
                BigDecimal totalHoursInvested = new BigDecimal(totalHoursInvesteds.get(i).toString());

                // 計算公式: totalValidHour / totalHoursInvested * 100
                BigDecimal timeEfficiency = totalValidHour.divide(totalHoursInvested, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                timeEfficiencys.add(timeEfficiency);
            }

            Map<String, List<Object>> newDataMap = new HashMap<>();
            newDataMap.put("timeEfficiency", timeEfficiencys);
            return newDataMap;
        } else {
            throw new RuntimeException("工時效率資料解析或計算有誤");
        }
    }
}
