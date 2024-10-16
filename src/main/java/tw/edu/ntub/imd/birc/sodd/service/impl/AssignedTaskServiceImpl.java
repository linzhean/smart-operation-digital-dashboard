package tw.edu.ntub.imd.birc.sodd.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.*;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.CalJsonToInfo;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.MailService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskTransformer;
import tw.edu.ntub.imd.birc.sodd.util.email.EmailUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class AssignedTaskServiceImpl extends BaseServiceImpl<AssignedTaskBean, AssignedTasks, Integer> implements AssignedTaskService {
    private final AssignedTaskDAO assignedTaskDAO;
    private final AssignedTaskTransformer transformer;
    private final ChartDAO chartDAO;
    private final DataSourceDAO dataSourceDAO;
    private final UserAccountDAO userAccountDAO;
    private final MailService mailService;

    public AssignedTaskServiceImpl(AssignedTaskDAO assignedTaskDAO,
                                   AssignedTaskTransformer transformer,
                                   ChartDAO chartDAO,
                                   DataSourceDAO dataSourceDAO,
                                   UserAccountDAO userAccountDAO,
                                   MailService mailService) {
        super(assignedTaskDAO, transformer);
        this.assignedTaskDAO = assignedTaskDAO;
        this.transformer = transformer;
        this.chartDAO = chartDAO;
        this.dataSourceDAO = dataSourceDAO;
        this.userAccountDAO = userAccountDAO;
        this.mailService = mailService;
    }


    @Override
    public AssignedTaskBean save(AssignedTaskBean assignedTaskBean) {
        AssignedTasks assignedTasks = transformer.transferToEntity(assignedTaskBean);
        return transformer.transferToBean(assignedTaskDAO.save(assignedTasks));
    }

    @Override
    public void checkChartDataIndicators() throws Exception {
        List<Chart> chartList = chartDAO.findByAvailableIsTrue();
        for (Chart chart : chartList) {
            AssignedTasks assignedTasks = assignedTaskDAO.findByChartIdAndAvailableIsTrue(chart.getId())
                    .stream()
                    .findFirst()
                    .orElse(new AssignedTasks());
            CalJsonToInfo calJsonToInfo = ChartDataSource.getCalJsonToInfo(
                    ChartDataSource.of(chart.getDataSource()));
            String jsonData = dataSourceDAO.getJsonData(ChartDataSource.of(chart.getDataSource()));
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, List<Object>>>() {
            }.getType();
            Map<String, List<Object>> calculatedData = gson.fromJson(jsonData, mapType);
            Map<String, List<Object>> newDataMap = calJsonToInfo.calJsonToInfo(calculatedData);
            newDataMap.forEach((key, ratios) -> {
                if (!ObjectUtils.isEmpty(assignedTasks)) {
                    isExceedLimits(assignedTasks, ratios, chart);
                }
            });
        }
    }

    private void isExceedLimits(AssignedTasks assignedTasks, List<Object> ratios, Chart chart) {
        UserAccount processor = userAccountDAO.findById(assignedTasks.getDefaultProcessor())
                .orElseThrow(() -> new NotFoundException("未設定系統自動交辦預設處理人"));
        UserAccount auditor = userAccountDAO.findById(assignedTasks.getDefaultAuditor())
                .orElseThrow(() -> new NotFoundException("未設定系統自動交辦預設稽核人"));
        for (Object object : ratios) {
            BigDecimal ratio = new BigDecimal(object.toString());
            if (assignedTasks.getUpperLimit() != null) {
                if (assignedTasks.getUpperLimit() < ratio.doubleValue()) {
                    mailService.sendSystemAssignedTask(assignedTasks, chart, ratio, processor, "高", assignedTasks.getUpperLimit());
                    mailService.sendSystemAssignedTask(assignedTasks, chart, ratio, auditor, "高", assignedTasks.getUpperLimit());
                    break;
                }
            }
            if (assignedTasks.getLowerLimit() != null) {
                if (assignedTasks.getLowerLimit() > ratio.doubleValue()) {
                    mailService.sendSystemAssignedTask(assignedTasks, chart, ratio, processor, "低", assignedTasks.getLowerLimit());
                    mailService.sendSystemAssignedTask(assignedTasks, chart, ratio, auditor, "低", assignedTasks.getLowerLimit());
                    break;
                }
            }
        }
    }
}
