package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskSponsorBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.AssignedTaskSponsorDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsorId;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskSponsorTransformer;

@Service
public class AssignedTaskSponsorServiceImpl extends BaseServiceImpl<AssignedTaskSponsorBean, AssignedTaskSponsor, AssignedTaskSponsorId>
        implements AssignedTaskSponsorService {
    private final AssignedTaskSponsorDAO assignedTaskSponsorDAO;
    private final AssignedTaskSponsorTransformer transformer;

    public AssignedTaskSponsorServiceImpl(AssignedTaskSponsorDAO assignedTaskSponsorDAO, AssignedTaskSponsorTransformer transformer) {
        super(assignedTaskSponsorDAO, transformer);
        this.assignedTaskSponsorDAO = assignedTaskSponsorDAO;
        this.transformer = transformer;
    }

    @Override
    public AssignedTaskSponsorBean save(AssignedTaskSponsorBean assignedTaskSponsorBean) {
        return null;
    }
}
