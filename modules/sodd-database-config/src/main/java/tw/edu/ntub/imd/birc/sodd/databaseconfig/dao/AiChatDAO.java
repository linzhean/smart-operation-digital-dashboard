package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AiChat;

import java.util.List;

@Primary
@Repository
public interface AiChatDAO extends BaseDAO<AiChat, Integer> {
    List<AiChat> findByChartIdAndCreateIdAndAvailableIsTrue(Integer chartId, String userId);

    List<AiChat> findByMessageIdAndAvailableIsTrue(Integer id);
}
