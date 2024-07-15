package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;
import tw.edu.ntub.imd.birc.sodd.service.transformer.GroupTransformer;

import javax.annotation.Nonnull;

@Component
public class GroupTransformerImpl implements GroupTransformer {
    @Nonnull
    @Override
    public Group transferToEntity(@Nonnull GroupBean groupBean) {
        return JavaBeanUtils.copy(groupBean, new Group());
    }

    @Nonnull
    @Override
    public GroupBean transferToBean(@Nonnull Group group) {
        return JavaBeanUtils.copy(group, new GroupBean());
    }
}
