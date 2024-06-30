package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.MailMessage;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailMessageTransformer;

import javax.annotation.Nonnull;

@Component
public class MailMessageTransformerImpl implements MailMessageTransformer {
    @Nonnull
    @Override
    public MailMessage transferToEntity(@Nonnull MailMessageBean mailMessageBean) {
        return JavaBeanUtils.copy(mailMessageBean, new MailMessage());
    }

    @Nonnull
    @Override
    public MailMessageBean transferToBean(@Nonnull MailMessage mailMessage) {
        return JavaBeanUtils.copy(mailMessage, new MailMessageBean());
    }
}
