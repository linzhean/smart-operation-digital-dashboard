package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroupId;

import java.util.List;

@Repository
public interface UserGroupDAO extends BaseDAO<UserGroup, UserGroupId>, JpaSpecificationExecutor<UserGroup> {
    List<UserGroup> findByUserIdAndAvailableIsTrue(String userId);
}
