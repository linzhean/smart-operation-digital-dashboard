package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;

import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardDAO extends BaseDAO<Dashboard, Integer> {
    List<Dashboard> findByCreateIdAndAvailableIsTrue(String userId);

    Optional<Dashboard> findByIdAndAvailableIsTrue(Integer id);
}
