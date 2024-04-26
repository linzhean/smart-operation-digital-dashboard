package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboardId;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartDashboardTransformer;

@Service
public class ChartDashboardServiceImpl extends BaseServiceImpl<ChartDashboardBean, ChartDashboard, ChartDashboardId> implements ChartDashboardService {
    private final ChartDashboardDAO chartDashboardDAO;
    private final ChartDashboardTransformer transformer;

    public ChartDashboardServiceImpl(ChartDashboardDAO chartDashboardDAO,
                                     ChartDashboardTransformer transformer) {
        super(chartDashboardDAO, transformer);
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
    }

    @Override
    public ChartDashboardBean save(ChartDashboardBean chartDashboardBean) {
        return null;
    }
}
