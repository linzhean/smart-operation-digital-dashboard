package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroupId;

@Repository
public interface ChartGroupDAO extends BaseDAO<ChartGroup, ChartGroupId> {

}
