package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserAccountTransformer;

import javax.annotation.Nonnull;

@Component
public class UserAccountTransformerImpl implements UserAccountTransformer {
    @Nonnull
    @Override
    public UserAccount transferToEntity(@Nonnull UserAccountBean userAccountBean) {
        return JavaBeanUtils.copy(userAccountBean, new UserAccount());
    }

    @Nonnull
    @Override
    public UserAccountBean transferToBean(@Nonnull UserAccount userAccount) {
        return JavaBeanUtils.copy(userAccount, new UserAccountBean());
    }
}
