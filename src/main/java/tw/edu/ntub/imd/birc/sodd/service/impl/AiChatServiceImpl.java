package tw.edu.ntub.imd.birc.sodd.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AiChatBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.AiChatDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DataSourceDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AiChat;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.CalJsonToInfo;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AiChatService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AiChatTransformer;
import tw.edu.ntub.imd.birc.sodd.util.sodd.PythonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Service
public class AiChatServiceImpl extends BaseServiceImpl<AiChatBean, AiChat, Integer> implements AiChatService {
    private final AiChatDAO aiChatDAO;
    private final DashboardDAO dashboardDAO;
    private final ChartDAO chartDAO;
    private final DataSourceDAO dataSourceDAO;
    private final PythonUtils pythonUtils;
    private final AiChatTransformer transformer;

    public AiChatServiceImpl(AiChatDAO aiChatDAO,
                             DashboardDAO dashboardDAO,
                             ChartDAO chartDAO,
                             DataSourceDAO dataSourceDAO,
                             PythonUtils pythonUtils,
                             AiChatTransformer transformer) {
        super(aiChatDAO, transformer);
        this.aiChatDAO = aiChatDAO;
        this.dashboardDAO = dashboardDAO;
        this.chartDAO = chartDAO;
        this.dataSourceDAO = dataSourceDAO;
        this.pythonUtils = pythonUtils;
        this.transformer = transformer;
    }


    @Override
    public AiChatBean save(AiChatBean aiChatBean) {
        AiChat aiChat = transformer.transferToEntity(aiChatBean);
        return transformer.transferToBean(aiChatDAO.save(aiChat));
    }

    @Override
    public String getChartSuggestion(Integer id, Integer dashboardId) throws Exception {
        Chart chart = chartDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        String jsonData = dataSourceDAO.getJsonData(ChartDataSource.of(chart.getDataSource()));
        CalJsonToInfo calJsonToInfo = ChartDataSource.getCalJsonToInfo(ChartDataSource.of(chart.getDataSource()));
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, List<Object>>>() {
        }.getType();
        Map<String, List<Object>> calculatedData = gson.fromJson(jsonData, mapType);
        Map<String, List<Object>> newInfoData = calJsonToInfo.calJsonToInfo(calculatedData);
        calculatedData.putAll(newInfoData);
        String calculatedJson = gson.toJson(calculatedData);
        String description = dashboardDAO.findById(dashboardId)
                .map(Dashboard::getDescription)
                .orElse("此儀表板尚無說明");
        return pythonUtils.genAISuggestion("python/llama3_ai/ai_suggestions.py", calculatedJson, description);
    }


    @Override
    public List<AiChatBean> searchByChartId(Integer chartId) {
        String userId = SecurityUtils.getLoginUserAccount();
        List<AiChat> resultList = new ArrayList<>();
        List<AiChat> messageList = aiChatDAO.findByChartIdAndCreateIdAndAvailableIsTrue(chartId, userId);
        if (messageList.isEmpty()) {
            return CollectionUtils.map(messageList, transformer::transferToBean);
        }
        AiChat firstMessage = messageList.stream()
                .filter(aiChat -> aiChat.getMessageId() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("查無此AI交談訊息"));
        return CollectionUtils.map(searchChildMessage(resultList, firstMessage), transformer::transferToBean);
    }


    @Override
    public String getNewChat(AiChatBean aiChatBean) throws IOException {
        save(aiChatBean);
        JSONArray jsonArray = new JSONArray();
        List<AiChatBean> aiChatList = searchByChartId(aiChatBean.getChartId());
        for (AiChatBean chatBean : aiChatList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("role", chatBean.getGenerator().getType());
            jsonObject.put("content", chatBean.getContent());
            jsonArray.add(jsonObject);
        }
        String jsonString = "\"" + jsonArray.toString()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"") + "\"";
        return pythonUtils.genAIChat("python/llama3_ai/ai_chat.py", jsonString);
    }


    private List<AiChat> searchChildMessage(List<AiChat> messageList, AiChat message) {
        messageList.add(message);
        Optional<AiChat> messageOptional = aiChatDAO
                .findByMessageIdAndAvailableIsTrue(message.getId())
                .stream()
                .filter(messageBean -> Objects.equals(messageBean.getMessageId(), message.getId()))
                .findAny();
        messageOptional.ifPresent(mailMessage -> searchChildMessage(messageList, mailMessage));
        return messageList;
    }
}
