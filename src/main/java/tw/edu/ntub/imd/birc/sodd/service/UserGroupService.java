package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroupId;

import java.util.List;

public interface UserGroupService extends BaseService<UserGroupBean, UserGroupId> {
    void removeUserFromGroup(String userId, Integer groupId);

    List<UserAccountBean> searchUserByGroupId(Integer groupId, String userName, String department, String position);
}
