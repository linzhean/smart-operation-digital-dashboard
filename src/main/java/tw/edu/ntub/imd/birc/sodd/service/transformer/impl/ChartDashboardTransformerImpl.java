package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartDashboardTransformer;

import javax.annotation.Nonnull;

@Component
public class ChartDashboardTransformerImpl implements ChartDashboardTransformer {
    @Nonnull
    @Override
    public ChartDashboard transferToEntity(@Nonnull ChartDashboardBean chartDashboardBean) {
        return JavaBeanUtils.copy(chartDashboardBean, new ChartDashboard());
    }

    @Nonnull
    @Override
    public ChartDashboardBean transferToBean(@Nonnull ChartDashboard chartDashboard) {
        return JavaBeanUtils.copy(chartDashboard, new ChartDashboardBean());
    }
}
