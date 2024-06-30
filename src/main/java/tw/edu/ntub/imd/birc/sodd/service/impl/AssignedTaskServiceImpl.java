package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.AssignedTaskDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskTransformer;

@Service
public class AssignedTaskServiceImpl extends BaseServiceImpl<AssignedTaskBean, AssignedTasks, Integer> implements AssignedTaskService {
    private final AssignedTaskDAO assignedTaskDAO;
    private final AssignedTaskTransformer transformer;

    public AssignedTaskServiceImpl(AssignedTaskDAO assignedTaskDAO, AssignedTaskTransformer transformer) {
        super(assignedTaskDAO, transformer);
        this.assignedTaskDAO = assignedTaskDAO;
        this.transformer = transformer;
    }


    @Override
    public AssignedTaskBean save(AssignedTaskBean assignedTaskBean) {
        AssignedTasks assignedTasks = transformer.transferToEntity(assignedTaskBean);
        return transformer.transferToBean(assignedTaskDAO.save(assignedTasks));
    }
}
