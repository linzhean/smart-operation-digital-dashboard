package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.SyncRecord;

import java.util.List;

@Repository
public interface SyncRecordDAO extends BaseDAO<SyncRecord, Integer> {
    List<SyncRecord> findByOrderBySyncTimeDesc();
}
