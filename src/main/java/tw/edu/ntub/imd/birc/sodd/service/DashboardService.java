package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.DashboardBean;

import java.util.List;
import java.util.Optional;

public interface DashboardService extends BaseService<DashboardBean, Integer> {
    List<DashboardBean> searchByUser(String userId);

    Optional<DashboardBean> findById(Integer id);
}
