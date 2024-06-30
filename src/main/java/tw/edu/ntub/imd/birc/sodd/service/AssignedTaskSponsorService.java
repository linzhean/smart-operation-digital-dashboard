package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskSponsorBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsorId;

import java.util.List;

public interface AssignedTaskSponsorService extends BaseService<AssignedTaskSponsorBean, AssignedTaskSponsorId> {
    List<AssignedTaskSponsorBean> findByUserId(String userId);

    List<AssignedTaskSponsorBean> finByChartId(Integer chartId);

    AssignedTaskSponsorBean save(Integer chartId, String userId);

    void removeSponsorFromChart(String userId, Integer chartId);
}
