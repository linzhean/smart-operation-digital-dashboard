package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroup;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartGroupTransformer;

import javax.annotation.Nonnull;

@Component
public class ChartGroupTransformerImpl implements ChartGroupTransformer {
    @Nonnull
    @Override
    public ChartGroup transferToEntity(@Nonnull ChartGroupBean chartGroupBean) {
        return JavaBeanUtils.copy(chartGroupBean, new ChartGroup());
    }

    @Nonnull
    @Override
    public ChartGroupBean transferToBean(@Nonnull ChartGroup chartGroup) {
        return JavaBeanUtils.copy(chartGroup, new ChartGroupBean());
    }
}
