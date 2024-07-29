package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.ChartGroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroupId;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroupId;
import tw.edu.ntub.imd.birc.sodd.service.ChartGroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartGroupTransformer;

@Service
public class ChartGroupServiceImpl extends BaseServiceImpl<ChartGroupBean, ChartGroup, ChartGroupId> implements ChartGroupService {
    private final ChartGroupDAO chartGroupDAO;
    private final ChartGroupTransformer transformer;

    public ChartGroupServiceImpl(ChartGroupDAO chartGroupDAO, ChartGroupTransformer transformer) {
        super(chartGroupDAO, transformer);
        this.chartGroupDAO = chartGroupDAO;
        this.transformer = transformer;
    }

    @Override
    public ChartGroupBean save(ChartGroupBean chartGroupBean) {
        ChartGroup chartGroup = transformer.transferToEntity(chartGroupBean);
        return transformer.transferToBean(chartGroupDAO.save(chartGroup));
    }

    @Override
    public void removeChartFromGroup(Integer chartId, Integer groupId) {
        ChartGroupBean chartGroupBean = new ChartGroupBean();
        chartGroupBean.setAvailable(false);
        ChartGroupId chartGroupId = new ChartGroupId();
        chartGroupId.setChartId(chartId);
        chartGroupId.setGroupId(groupId);
        super.update(chartGroupId, chartGroupBean);
    }
}
