package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.ChartGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroupId;
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
        return null;
    }
}
