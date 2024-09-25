package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.RequestBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Request;
import tw.edu.ntub.imd.birc.sodd.service.transformer.RequestTransformer;

import javax.annotation.Nonnull;

@Component
public class RequestTransformerImpl implements RequestTransformer {
    @Nonnull
    @Override
    public Request transferToEntity(@Nonnull RequestBean requestBean) {
        return JavaBeanUtils.copy(requestBean, new Request());
    }

    @Nonnull
    @Override
    public RequestBean transferToBean(@Nonnull Request request) {
        return JavaBeanUtils.copy(request, new RequestBean());
    }
}
