package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.AiChatBean;

import java.io.IOException;
import java.util.List;

public interface AiChatService extends BaseService<AiChatBean, Integer> {
    String getChartSuggestion(Integer id, Integer dashboardId) throws Exception;

    List<AiChatBean> searchByChartId(Integer chartId);

    String getNewChat(AiChatBean aiChatBean) throws IOException;
}
