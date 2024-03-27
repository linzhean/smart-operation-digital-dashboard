package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;

@Repository
public interface UserAccountDAO extends BaseDAO<UserAccount, String> {
}
