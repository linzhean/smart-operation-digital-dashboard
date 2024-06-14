package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;

@Repository
public interface AssignedTaskDAO extends BaseDAO<AssignedTasks, Integer> {
}
