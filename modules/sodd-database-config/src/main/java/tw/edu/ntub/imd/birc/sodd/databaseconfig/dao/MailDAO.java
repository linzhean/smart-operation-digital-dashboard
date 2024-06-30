package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;

import java.util.List;

@Repository
public interface MailDAO extends BaseDAO<Mail, Integer>, JpaSpecificationExecutor<Mail> {

}
