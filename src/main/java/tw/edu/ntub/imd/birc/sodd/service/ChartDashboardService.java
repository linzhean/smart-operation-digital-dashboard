package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;

import java.util.List;

public interface ChartDashboardService extends BaseService<ChartDashboardBean, Integer> {
    ChartDashboardBean save(Integer chartId, Integer dashboardId);

    List<ChartDashboardBean> findByDashboardId(Integer dashboardId);

    List<ChartDashboardBean> findByChartId(Integer chartId);
}
