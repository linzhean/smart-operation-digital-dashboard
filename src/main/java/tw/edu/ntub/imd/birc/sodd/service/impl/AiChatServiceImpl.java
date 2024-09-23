package tw.edu.ntub.imd.birc.sodd.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AiChatBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.AiChatDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AiChat;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AiChatService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AiChatTransformer;
import tw.edu.ntub.imd.birc.sodd.util.python.PythonUtils;

import java.io.IOException;
import java.util.*;

@Service
public class AiChatServiceImpl extends BaseServiceImpl<AiChatBean, AiChat, Integer> implements AiChatService {
    private final AiChatDAO aiChatDAO;
    private final DashboardDAO dashboardDAO;
    private final ChartDAO chartDAO;
    private final PythonUtils pythonUtils;
    private final AiChatTransformer transformer;

    public AiChatServiceImpl(AiChatDAO aiChatDAO,
                             DashboardDAO dashboardDAO,
                             ChartDAO chartDAO,
                             PythonUtils pythonUtils,
                             AiChatTransformer transformer) {
        super(aiChatDAO, transformer);
        this.aiChatDAO = aiChatDAO;
        this.dashboardDAO = dashboardDAO;
        this.chartDAO = chartDAO;
        this.pythonUtils = pythonUtils;
        this.transformer = transformer;
    }


    @Override
    public AiChatBean save(AiChatBean aiChatBean) {
        AiChat aiChat = transformer.transferToEntity(aiChatBean);
        return transformer.transferToBean(aiChatDAO.save(aiChat));
    }

    @Override
    public String getChartSuggestion(Integer id, Integer dashboardId) throws IOException {
        String chartData = "| 季度     | 銷售額 (美元) | 客戶數量 | 新增客戶數 | 客戶流失率 | 主要產品銷售額 (美元) |" +
                "|---------|-------------|---------|-----------|-----------|------------------| " +
                "| 2023 Q1 | 200,000     | 255     | 20        | 15%       | 30,000           | " +
                "| 2023 Q2 | 250,000     | 270     | 25        | 10%       | 40,000           | " +
                "| 2023 Q3 | 180,000     | 240     | 15        | 20%       | 20,000           | " +
                "| 2023 Q4 | 220,000     | 260     | 30        | 12%       | 35,000           |";
        // TODO 之後有圖表程式的時候才會開啟
        Chart chart = chartDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        String description = dashboardDAO.findById(dashboardId)
                .map(Dashboard::getDescription)
                .orElse("");
        return pythonUtils.genAISuggestion("python/llama3_ai/ai_suggestions.py", chartData, description);
    }


    @Override
    public List<AiChatBean> searchByChartId(Integer chartId) {
        List<AiChat> resultList = new ArrayList<>();
        List<AiChat> messageList = aiChatDAO.findByChartIdAndAvailableIsTrue(chartId);
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
