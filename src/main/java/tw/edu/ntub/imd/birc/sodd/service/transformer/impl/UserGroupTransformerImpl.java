package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserGroupTransformer;

import javax.annotation.Nonnull;

@Component
public class UserGroupTransformerImpl implements UserGroupTransformer {
    @Nonnull
    @Override
    public UserGroup transferToEntity(@Nonnull UserGroupBean userGroupBean) {
        return JavaBeanUtils.copy(userGroupBean, new UserGroup());
    }

    @Nonnull
    @Override
    public UserGroupBean transferToBean(@Nonnull UserGroup userGroup) {
        return JavaBeanUtils.copy(userGroup, new UserGroupBean());
    }
}
