package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.MailMessage;

import java.util.List;

@Repository
public interface MailMessageDAO extends BaseDAO<MailMessage, Integer> {
    List<MailMessage> findByMailId(Integer mailId);
}
