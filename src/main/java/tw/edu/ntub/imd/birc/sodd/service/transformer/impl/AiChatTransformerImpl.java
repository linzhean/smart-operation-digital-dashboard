package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AiChatBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AiChat;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AiChatTransformer;

import javax.annotation.Nonnull;

@Component
public class AiChatTransformerImpl implements AiChatTransformer {
    @Nonnull
    @Override
    public AiChat transferToEntity(@Nonnull AiChatBean aiChatBean) {
        return JavaBeanUtils.copy(aiChatBean, new AiChat());
    }

    @Nonnull
    @Override
    public AiChatBean transferToBean(@Nonnull AiChat aiChat) {
        return JavaBeanUtils.copy(aiChat, new AiChatBean());
    }
}
