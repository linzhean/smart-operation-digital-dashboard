package tw.edu.ntub.imd.birc.sodd.service;

import org.springframework.core.io.Resource;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ChartService extends BaseService<ChartBean, Integer> {
    List<ChartBean> searchByDashboardId(Integer dashboardId);

    String genChartHTML(ChartBean chartBean);

    List<ChartBean> searchByUser(String userId);

    List<ChartBean> searchByAvailable(Boolean available);
}
