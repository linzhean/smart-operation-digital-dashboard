package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;

import java.util.List;

@Repository
public interface ChartGroupDAO extends BaseDAO<ChartGroup, Integer> {
    List<ChartGroup> findByGroupIdAndAvailableIsTrue(Integer groupId);
}
