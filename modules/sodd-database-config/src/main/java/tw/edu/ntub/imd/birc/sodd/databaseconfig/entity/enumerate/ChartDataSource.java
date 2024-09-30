package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.CalJsonToInfo;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.RefundRate;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.TimeEfficiency;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.YieldAchievementRate;

@Getter
public enum ChartDataSource {
    NO_SUCH_TABLE(""),
    YIELD_ACHIEVEMENT_RATE("yield_achievement_rate"),
    REFUND_RATE("refund_rate"),
    TIME_EFFICIENCY("time_efficiency");

    private final String value;

    ChartDataSource(String value) {
        this.value = value;
    }

    public static ChartDataSource of(String value) {
        for (ChartDataSource chartDataSource : ChartDataSource.values()) {
            if (chartDataSource.getValue().equals(value)) {
                return chartDataSource;
            }
        }
        return ChartDataSource.NO_SUCH_TABLE;
    }

    public static Class<?> getChartClass(ChartDataSource chartDataSource) {
        switch (chartDataSource) {
            case YIELD_ACHIEVEMENT_RATE:
                return YieldAchievementRate.class;
            case TIME_EFFICIENCY:
                return TimeEfficiency.class;
            case REFUND_RATE:
                return RefundRate.class;
            default:
                throw new RuntimeException("查無此dataSource對應的資料表");
        }
    }

    public static CalJsonToInfo getCalJsonToInfo(ChartDataSource chartDataSource) {
        switch (chartDataSource) {
            case YIELD_ACHIEVEMENT_RATE:
                return new YieldAchievementRate();
            case TIME_EFFICIENCY:
                return new TimeEfficiency();
            case REFUND_RATE:
                return new RefundRate();
            default:
                throw new RuntimeException("查無此dataSource對應的資料表");
        }
    }
}
