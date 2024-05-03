package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;

import java.util.List;

public interface ChartService extends BaseService<ChartBean, Integer> {
    List<ChartBean> searchByDashboardId(Integer dashboardId);

    List<Integer> searchChartIdsByDashboardId(Integer dashboardId);

    List<ChartBean> searchByUser(String userId);
}
