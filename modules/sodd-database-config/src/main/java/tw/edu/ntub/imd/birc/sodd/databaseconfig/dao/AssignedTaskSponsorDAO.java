package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;

import java.util.List;

@Repository
public interface AssignedTaskSponsorDAO extends BaseDAO<AssignedTaskSponsor, Integer> {
    List<AssignedTaskSponsor> findBySponsorUserIdAndAvailableIsTrue(String userId);

    List<AssignedTaskSponsor> findByChartIdAndAvailableIsTrue(Integer chartId);
}
