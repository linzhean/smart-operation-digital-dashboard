package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboardId;

public interface ChartDashboardService extends BaseService<ChartDashboardBean, ChartDashboardId> {
    void removeChartFromDashboard(Integer chartId, Integer dashboardId);

    ChartDashboardBean save(Integer chartId, Integer dashboardId);
}
