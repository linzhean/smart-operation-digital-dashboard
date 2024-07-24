package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExportService extends BaseService<ExportBean, Integer> {
    List<ExportBean> findByChartId(Integer chartId);

    ExportBean save(Integer chartId, String userId);

    void export(Integer chartId, HttpServletResponse response);
}
