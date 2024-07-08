package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ExportDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Export;
import tw.edu.ntub.imd.birc.sodd.service.ExportService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ExportTransformer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class ExportServiceImpl extends BaseServiceImpl<ExportBean, Export, Integer> implements ExportService {
    private final ExportDAO exportDAO;
    private final ExportTransformer transformer;

    public ExportServiceImpl(ExportDAO exportDAO, ExportTransformer transformer) {
        super(exportDAO, transformer);
        this.exportDAO = exportDAO;
        this.transformer = transformer;
    }

    @Override
    public ExportBean save(ExportBean exportBean) {
        Export export = transformer.transferToEntity(exportBean);
        return transformer.transferToBean(exportDAO.save(export));
    }

    @Override
    public List<ExportBean> findByChartId(Integer chartId) {
        return CollectionUtils.map(
                exportDAO.findByChartIdAndAvailableIsTrue(chartId), transformer::transferToBean);
    }

    @Override
    public ExportBean save(Integer chartId, String userId) {
        Export export = new Export();
        export.setChartId(chartId);
        export.setExporter(userId);
        return transformer.transferToBean(exportDAO.save(export));
    }

    @Override
    public void export(Integer chartId, HttpServletResponse response) {

    }
}
