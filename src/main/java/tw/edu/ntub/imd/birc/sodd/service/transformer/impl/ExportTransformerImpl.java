package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Export;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ExportTransformer;

import javax.annotation.Nonnull;

@Component
public class ExportTransformerImpl implements ExportTransformer {
    @Nonnull
    @Override
    public Export transferToEntity(@Nonnull ExportBean exportBean) {
        return JavaBeanUtils.copy(exportBean, new Export());
    }

    @Nonnull
    @Override
    public ExportBean transferToBean(@Nonnull Export export) {
        return JavaBeanUtils.copy(export, new ExportBean());
    }
}
