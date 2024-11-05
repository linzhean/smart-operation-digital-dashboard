package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;

import java.util.List;

public interface GroupService extends BaseService<GroupBean, Integer> {
    List<GroupBean> searchByUserId(String userId);

    void delGroup(Integer groupId);

    void checkNotAccessibleChart(String userId);
}
