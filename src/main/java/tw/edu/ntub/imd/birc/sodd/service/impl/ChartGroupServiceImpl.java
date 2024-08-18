package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartGroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartGroupTransformer;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartGroupServiceImpl extends BaseServiceImpl<ChartGroupBean, ChartGroup, Integer> implements ChartGroupService {
    private final ChartGroupDAO chartGroupDAO;
    private final ChartGroupTransformer transformer;
    private final ChartDAO chartDAO;
    private final ChartTransformer chartTransformer;

    public ChartGroupServiceImpl(ChartGroupDAO chartGroupDAO,
                                 ChartGroupTransformer transformer,
                                 ChartDAO chartDAO,
                                 ChartTransformer chartTransformer) {
        super(chartGroupDAO, transformer);
        this.chartGroupDAO = chartGroupDAO;
        this.transformer = transformer;
        this.chartDAO = chartDAO;
        this.chartTransformer = chartTransformer;
    }

    @Override
    public ChartGroupBean save(ChartGroupBean chartGroupBean) {
        ChartGroup chartGroup = transformer.transferToEntity(chartGroupBean);
        return transformer.transferToBean(chartGroupDAO.save(chartGroup));
    }

    @Override
    public List<ChartBean> searchChartByGroupId(Integer groupId) {
        return chartGroupDAO.findByGroupIdAndAvailableIsTrue(groupId)
                .stream()
                .map(chartGroup -> {
                    ChartBean chartBean = chartTransformer.transferToBean(
                            chartDAO.findById(chartGroup.getChartId())
                                    .orElseThrow(() -> new NotFoundException("查無此圖表")));
                    chartBean.setChartGroupId(chartGroup.getId());
                    return chartBean;
                })
                .collect(Collectors.toList());
    }
}
