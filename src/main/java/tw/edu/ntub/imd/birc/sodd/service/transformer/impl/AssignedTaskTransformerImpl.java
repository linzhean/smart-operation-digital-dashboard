package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskTransformer;

import javax.annotation.Nonnull;

@Component
public class AssignedTaskTransformerImpl implements AssignedTaskTransformer {
    @Nonnull
    @Override
    public AssignedTasks transferToEntity(@Nonnull AssignedTaskBean assignedTaskBean) {
        return JavaBeanUtils.copy(assignedTaskBean, new AssignedTasks());
    }

    @Nonnull
    @Override
    public AssignedTaskBean transferToBean(@Nonnull AssignedTasks assignedTasks) {
        return JavaBeanUtils.copy(assignedTasks, new AssignedTaskBean());
    }
}
