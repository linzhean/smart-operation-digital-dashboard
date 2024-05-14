package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;

@Repository
public interface ApplicationDAO extends BaseDAO<Application, Integer>, JpaSpecificationExecutor<Application> {
}
