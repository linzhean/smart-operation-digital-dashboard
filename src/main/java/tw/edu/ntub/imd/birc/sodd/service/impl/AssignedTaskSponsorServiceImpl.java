package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskSponsorBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.AssignedTaskSponsorDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsorId;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.AssignedTaskSponsorTransformer;

import java.util.List;

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

    @Override
    public List<AssignedTaskSponsorBean> findByUserId(String userId) {
        return CollectionUtils.map(
                assignedTaskSponsorDAO.findBySponsorUserIdAndAvailableIsTrue(userId), transformer::transferToBean);
    }

    @Override
    public List<AssignedTaskSponsorBean> findByChartId(Integer chartId) {
        return CollectionUtils.map(
                assignedTaskSponsorDAO.findByChartIdAndAvailableIsTrue(chartId), transformer::transferToBean);
    }

    @Override
    public AssignedTaskSponsorBean save(Integer chartId, String userId) {
        AssignedTaskSponsor sponsor = new AssignedTaskSponsor();
        sponsor.setChartId(chartId);
        sponsor.setSponsorUserId(userId);
        return transformer.transferToBean(assignedTaskSponsorDAO.save(sponsor));
    }

    @Override
    public void removeSponsorFromChart(Integer chartId, String userId) {
        AssignedTaskSponsorBean sponsorBean = new AssignedTaskSponsorBean();
        sponsorBean.setAvailable(false);
        AssignedTaskSponsorId sponsorId = new AssignedTaskSponsorId();
        sponsorId.setSponsorUserId(userId);
        sponsorId.setChartId(chartId);
        update(sponsorId, sponsorBean);
    }
}
