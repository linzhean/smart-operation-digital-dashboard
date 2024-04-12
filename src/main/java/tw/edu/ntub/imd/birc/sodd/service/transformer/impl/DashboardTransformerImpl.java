package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.DashboardBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.service.transformer.DashboardTransformer;

import javax.annotation.Nonnull;

@Component
public class DashboardTransformerImpl implements DashboardTransformer {
    @Nonnull
    @Override
    public Dashboard transferToEntity(@Nonnull DashboardBean dashboardBean) {
        return JavaBeanUtils.copy(dashboardBean, new Dashboard());
    }

    @Nonnull
    @Override
    public DashboardBean transferToBean(@Nonnull Dashboard dashboard) {
        return JavaBeanUtils.copy(dashboard, new DashboardBean());
    }
}
