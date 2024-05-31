package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Groups;
import java.util.List;

@Repository
public interface GroupDAO extends BaseDAO<Groups, Integer> {
    List<Groups> findByAvailableIsTrue();
}
