package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;

import java.util.List;

public interface AssignedTaskService extends BaseService<AssignedTaskBean, Integer> {
    void checkChartDataIndicators() throws Exception;
}
