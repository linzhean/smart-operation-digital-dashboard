package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboardId;

import java.util.List;

@Repository
public interface ChartDashboardDAO extends BaseDAO<ChartDashboard, ChartDashboardId> {
    List<ChartDashboard> findByDashboardIdAndAvailableIsTrue(Integer chartDashboardId);
}
