package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;
import java.util.List;

public interface GroupDAO extends BaseDAO<Group, Integer> {
    List<Group> findByAvailableIsTrue();
}
