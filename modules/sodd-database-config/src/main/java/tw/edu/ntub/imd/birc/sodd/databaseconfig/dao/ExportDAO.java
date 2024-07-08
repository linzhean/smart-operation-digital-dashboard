package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Export;

import java.util.List;

@Repository
public interface ExportDAO extends BaseDAO<Export, Integer> {
    List<Export> findByChartIdAndAvailableIsTrue(Integer chartId);
}
