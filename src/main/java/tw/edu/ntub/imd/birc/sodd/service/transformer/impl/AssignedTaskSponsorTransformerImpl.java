package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskSponsorBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskSponsorTransformer;

import javax.annotation.Nonnull;

@Component
public class AssignedTaskSponsorTransformerImpl implements AssignedTaskSponsorTransformer {
    @Nonnull
    @Override
    public AssignedTaskSponsor transferToEntity(@Nonnull AssignedTaskSponsorBean assignedTaskSponsorBean) {
        return JavaBeanUtils.copy(assignedTaskSponsorBean, new AssignedTaskSponsor());
    }

    @Nonnull
    @Override
    public AssignedTaskSponsorBean transferToBean(@Nonnull AssignedTaskSponsor assignedTaskSponsor) {
        return JavaBeanUtils.copy(assignedTaskSponsor, new AssignedTaskSponsorBean());
    }
}
