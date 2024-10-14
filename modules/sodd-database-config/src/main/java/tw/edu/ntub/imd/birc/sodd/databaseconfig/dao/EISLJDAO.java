package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp.EISLJ;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp.EISLJId;

@Repository
public interface EISLJDAO extends BaseDAO<EISLJ, EISLJId> {
}
