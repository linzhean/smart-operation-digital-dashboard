package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.DashboardBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.service.DashboardService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.DashboardTransformer;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardServiceImpl extends BaseServiceImpl<DashboardBean, Dashboard, Integer> implements DashboardService {
    private final DashboardDAO dashboardDAO;
    private final DashboardTransformer transformer;


    public DashboardServiceImpl(DashboardDAO dashboardDAO, DashboardTransformer transformer) {
        super(dashboardDAO, transformer);
        this.dashboardDAO = dashboardDAO;
        this.transformer = transformer;
    }

    @Override
    public DashboardBean save(DashboardBean dashboardBean) {
        Dashboard dashboard = transformer.transferToEntity(dashboardBean);
        return transformer.transferToBean(dashboardDAO.save(dashboard));
    }

    @Override
    public List<DashboardBean> searchByUser(String userId) {
        return CollectionUtils.map(dashboardDAO.findByCreateIdAndAvailableIsTrue(), transformer::transferToBean);
    }

    @Override
    public Optional<DashboardBean> findById(Integer id) {
        Optional<Dashboard> dashboardOptional = dashboardDAO.findByIdAndAvailableIsTrue(id);
        if (dashboardOptional.isPresent()) {
            Dashboard dashboard = dashboardOptional.get();
            return Optional.of(transformer.transferToBean(dashboard));
        } else {
            return Optional.empty();
        }
    }
}
