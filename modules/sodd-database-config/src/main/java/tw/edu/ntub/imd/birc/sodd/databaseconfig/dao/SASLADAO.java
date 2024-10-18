package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp.SASLA;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp.SASLAId;

@Repository
public interface SASLADAO extends BaseDAO<SASLA, SASLAId> {
}
