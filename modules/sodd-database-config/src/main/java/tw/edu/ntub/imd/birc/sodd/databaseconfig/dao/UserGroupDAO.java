package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;

import java.util.List;

public interface UserGroupDAO extends BaseDAO<UserGroup, Integer> {
    List<UserGroup> findByUserIdAndAvailableIsTrue(String userId);
}
