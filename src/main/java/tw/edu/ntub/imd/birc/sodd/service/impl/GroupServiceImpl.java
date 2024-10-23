package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.*;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.*;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.GroupTransformer;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends BaseServiceImpl<GroupBean, Group, Integer> implements GroupService {
    private final GroupDAO groupDAO;
    private final UserGroupDAO userGroupDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartDAO chartDAO;
    private final ChartDashboardDAO chartDashboardDAO;
    private final GroupTransformer transformer;

    public GroupServiceImpl(GroupDAO groupDAO,
                            UserGroupDAO userGroupDAO,
                            GroupTransformer transformer,
                            ChartGroupDAO chartGroupDAO,
                            ChartDAO chartDAO,
                            ChartDashboardDAO chartDashboardDAO) {
        super(groupDAO, transformer);
        this.groupDAO = groupDAO;
        this.userGroupDAO = userGroupDAO;
        this.transformer = transformer;
        this.chartGroupDAO = chartGroupDAO;
        this.chartDAO = chartDAO;
        this.chartDashboardDAO = chartDashboardDAO;
    }


    @Override
    public GroupBean save(GroupBean groupBean) {
        Group group = transformer.transferToEntity(groupBean);
        return transformer.transferToBean(groupDAO.save(group));
    }

    @Override
    public List<GroupBean> searchByUserId(String userId) {
        List<Group> groups = userGroupDAO.findByUserIdAndAvailableIsTrue(userId)
                .stream()
                .map(userGroup -> groupDAO.findById(userGroup.getGroupId()).orElse(null))
                .filter(group -> group != null && group.getAvailable())
                .collect(Collectors.toList());
        return CollectionUtils.map(groups, transformer::transferToBean);
    }

    @Override
    public void delGroup(Integer groupId) {
        GroupBean groupBean = new GroupBean();
        groupBean.setAvailable(false);
        for (UserGroup userGroup : userGroupDAO.findByGroupIdAndAvailableIsTrue(groupId)) {
            userGroup.setAvailable(false);
            userGroupDAO.update(userGroup);
        }
        for (ChartGroup chartGroup : chartGroupDAO.findByGroupIdAndAvailableIsTrue(groupId)) {
            chartGroup.setAvailable(false);
            chartGroupDAO.update(chartGroup);
        }
        update(groupId, groupBean);
    }

    @Override
    public void checkNotAccessibleChart(String userId) {
        List<Chart> observableCharts = searchByUserId(userId)
                .stream()
                .flatMap(groupBean -> chartGroupDAO.findByGroupIdAndAvailableIsTrue(groupBean.getId()).stream())
                .map(chartGroup -> chartDAO.findByIdAndAvailableIsTrue(chartGroup.getChartId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<ChartDashboard> chartDashboards = chartDashboardDAO.findByCreateIdAndAvailableIsTrue(userId);
        // 取得所有 observableCharts 的 chartId
        Set<Integer> observableChartIds = observableCharts.stream()
                .map(Chart::getId)
                .collect(Collectors.toSet());

        // 找出無法配對的 chartDashboard
        List<ChartDashboard> unmatchableDashboards = chartDashboards.stream()
                .filter(chartDashboard -> !observableChartIds.contains(chartDashboard.getChartId()))
                .collect(Collectors.toList());

        // 輸出無法配對的 chartDashboard
        if (!unmatchableDashboards.isEmpty()) {
            unmatchableDashboards.forEach(unmatched -> {
                unmatched.setAvailable(false);
                chartDashboardDAO.update(unmatched);
            });
        }
    }

    @Override
    public List<GroupBean> searchAll() {
        return CollectionUtils.map(groupDAO.findByAvailableIsTrue(), transformer::transferToBean);
    }
}
