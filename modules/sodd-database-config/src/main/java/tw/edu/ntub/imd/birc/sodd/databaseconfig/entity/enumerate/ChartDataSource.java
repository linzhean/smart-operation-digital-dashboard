package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.*;

@Getter
public enum ChartDataSource {
    NO_SUCH_TABLE(""),
    YIELD_ACHIEVEMENT_RATE("yield_achievement_rate"),
    REFUND_RATE("refund_rate"),
    TIME_EFFICIENCY("time_efficiency"),
    SUBCONTRACTING_REFUND_RATE("subcontracting_refund_rate"),
    PURCHASE_UNIT_PRICE("purchase_unit_price"),
    TURNOVER_RATE("turnover_rate"),
    INVENTORY_LEVELS("inventory_levels"),
    COST_VARIANCE_RATE("cost_variance_rate");

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
            case SUBCONTRACTING_REFUND_RATE:
                return SubcontractingRefundRate.class;
            case COST_VARIANCE_RATE:
                return CostVarianceRate.class;
            case PURCHASE_UNIT_PRICE:
                return PurchaseUnitPrice.class;
            case TURNOVER_RATE:
                return TurnoverRate.class;
            case INVENTORY_LEVELS:
                return InventoryLevels.class;
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
            case SUBCONTRACTING_REFUND_RATE:
                return new SubcontractingRefundRate();
            case COST_VARIANCE_RATE:
                return new CostVarianceRate();
            case PURCHASE_UNIT_PRICE:
                return new PurchaseUnitPrice();
            case TURNOVER_RATE:
                return new TurnoverRate();
            case INVENTORY_LEVELS:
                return new InventoryLevels();
            default:
                throw new RuntimeException("查無此dataSource對應的資料表");
        }
    }
}
