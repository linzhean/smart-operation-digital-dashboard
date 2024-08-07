package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboardId;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartDashboardTransformer;

import java.util.Objects;

@Service
public class ChartDashboardServiceImpl extends BaseServiceImpl<ChartDashboardBean, ChartDashboard, ChartDashboardId>
        implements ChartDashboardService {
    private final ChartDashboardDAO chartDashboardDAO;
    private final ChartDashboardTransformer transformer;
    private final UserGroupDAO userGroupDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartService chartService;

    public ChartDashboardServiceImpl(ChartDashboardDAO chartDashboardDAO,
                                     ChartDashboardTransformer transformer,
                                     UserGroupDAO userGroupDAO,
                                     ChartGroupDAO chartGroupDAO,
                                     ChartService chartService) {
        super(chartDashboardDAO, transformer);
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
        this.userGroupDAO = userGroupDAO;
        this.chartGroupDAO = chartGroupDAO;
        this.chartService = chartService;
    }

    @Override
    public ChartDashboardBean save(ChartDashboardBean chartDashboardBean) {
        return null;
    }

    @Override
    public void removeChartFromDashboard(Integer chartId, Integer dashboardId) {
        ChartDashboardBean chartDashboardBean = new ChartDashboardBean();
        chartDashboardBean.setAvailable(false);
        ChartDashboardId chartDashboardId = new ChartDashboardId();
        chartDashboardId.setChartId(chartId);
        chartDashboardId.setDashboardId(dashboardId);
        update(chartDashboardId, chartDashboardBean);
    }

    @Override
    public ChartDashboardBean save(Integer chartId, Integer dashboardId) {
        String userId = SecurityUtils.getLoginUserAccount();
        boolean observable = userGroupDAO.findByUserIdAndAvailableIsTrue(userId).stream()
                .flatMap(userGroup ->
                        chartGroupDAO.findByGroupIdAndAvailableIsTrue(userGroup.getGroupId()).stream())
                .anyMatch(chartGroup -> Objects.equals(chartGroup.getChartId(), chartId));
        ChartDashboard chartDashboard = new ChartDashboard();
        chartDashboard.setChartId(chartId);
        chartDashboard.setDashboardId(dashboardId);
        if (observable) {
            return transformer.transferToBean(chartDashboardDAO.save(chartDashboard));
        }
        String chartName = chartService.getById(chartId)
                .map(ChartBean::getName)
                .orElse("");
        throw new AccessDeniedException("您無法添加" + chartName + "至您的儀表板");
    }
}
