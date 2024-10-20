package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;

import java.util.List;

public interface UserGroupService extends BaseService<UserGroupBean, Integer> {
    List<UserAccountBean> searchUserByGroupId(Integer groupId, String userName, String department, String position);

    List<UserAccountBean> searchUserByGroupId(Integer groupId);

    List<UserGroupBean> searchUserGroupByGroupId(Integer groupId);
}
