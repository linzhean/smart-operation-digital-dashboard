package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailTransformer;

import javax.annotation.Nonnull;

@Component
public class MailTransformerImpl implements MailTransformer {
    @Nonnull
    @Override
    public Mail transferToEntity(@Nonnull MailBean mailBean) {
        return JavaBeanUtils.copy(mailBean, new Mail());
    }

    @Nonnull
    @Override
    public MailBean transferToBean(@Nonnull Mail mail) {
        return JavaBeanUtils.copy(mail, new MailBean());
    }
}
