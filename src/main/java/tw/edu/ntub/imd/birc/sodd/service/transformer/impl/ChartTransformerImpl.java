package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;

import javax.annotation.Nonnull;

@Component
public class ChartTransformerImpl implements ChartTransformer {
    @Nonnull
    @Override
    public Chart transferToEntity(@Nonnull ChartBean chartBean) {
        return JavaBeanUtils.copy(chartBean, new Chart());
    }

    @Nonnull
    @Override
    public ChartBean transferToBean(@Nonnull Chart chart) {
        return JavaBeanUtils.copy(chart, new ChartBean());
    }
}
