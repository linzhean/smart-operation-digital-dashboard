package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ApplicationBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ApplicationTransformer;

import javax.annotation.Nonnull;

@Component
public class ApplicationTransformerImpl implements ApplicationTransformer {
    @Nonnull
    @Override
    public Application transferToEntity(@Nonnull ApplicationBean applicationBean) {
        return JavaBeanUtils.copy(applicationBean, new Application());
    }

    @Nonnull
    @Override
    public ApplicationBean transferToBean(@Nonnull Application application) {
        return JavaBeanUtils.copy(application, new ApplicationBean());
    }
}
