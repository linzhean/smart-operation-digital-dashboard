package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

@Getter
public enum ChartDataSource {
    NO_SUCH_TABLE(""),
    YIELD_ACHIEVEMENT_RATE("yield_achievement_rate");

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
}
