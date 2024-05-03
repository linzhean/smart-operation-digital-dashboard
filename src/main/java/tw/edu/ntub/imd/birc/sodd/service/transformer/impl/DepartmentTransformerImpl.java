package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.DepartmentBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Department;
import tw.edu.ntub.imd.birc.sodd.service.transformer.DepartmentTransformer;

import javax.annotation.Nonnull;

@Component
public class DepartmentTransformerImpl implements DepartmentTransformer {
    @Nonnull
    @Override
    public Department transferToEntity(@Nonnull DepartmentBean departmentBean) {
        return JavaBeanUtils.copy(departmentBean, new Department());
    }

    @Nonnull
    @Override
    public DepartmentBean transferToBean(@Nonnull Department department) {
        return JavaBeanUtils.copy(department, new DepartmentBean());
    }
}
