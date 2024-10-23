package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;

import java.util.List;

@Repository
public interface ChartDashboardDAO extends BaseDAO<ChartDashboard, Integer> {
    List<ChartDashboard> findByDashboardIdAndAvailableIsTrue(Integer chartDashboardId);

    List<ChartDashboard> findByChartIdAndAvailableIsTrue(Integer chartDashboardId);

    List<ChartDashboard> findByCreateIdAndAvailableIsTrue(String createId);
}
