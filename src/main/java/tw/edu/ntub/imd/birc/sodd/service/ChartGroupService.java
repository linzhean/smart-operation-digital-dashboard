package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartGroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;

import java.util.List;

public interface ChartGroupService extends BaseService<ChartGroupBean, Integer> {
    List<ChartBean> searchChartByGroupId(Integer groupId);
}
