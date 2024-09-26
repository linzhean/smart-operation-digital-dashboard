package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DataSourceDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ExportDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Export;
import tw.edu.ntub.imd.birc.sodd.dto.excel.row.Row;
import tw.edu.ntub.imd.birc.sodd.dto.excel.sheet.Sheet;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.PoiWorkbook;
import tw.edu.ntub.imd.birc.sodd.dto.excel.workbook.Workbook;
import tw.edu.ntub.imd.birc.sodd.enumerate.ExcelType;
import tw.edu.ntub.imd.birc.sodd.exception.ChartException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ExportService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ExportTransformer;
import tw.edu.ntub.imd.birc.sodd.util.excel.ExcelUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ExcelResponseUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ExportServiceImpl extends BaseServiceImpl<ExportBean, Export, Integer> implements ExportService {
    private final ExportDAO exportDAO;
    private final ChartDAO chartDAO;
    private final DataSourceDAO dataSourceDAO;
    private final ExportTransformer transformer;

    public ExportServiceImpl(ExportDAO exportDAO,
                             ChartDAO chartDAO,
                             DataSourceDAO dataSourceDAO,
                             ExportTransformer transformer) {
        super(exportDAO, transformer);
        this.exportDAO = exportDAO;
        this.chartDAO = chartDAO;
        this.dataSourceDAO = dataSourceDAO;
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
        boolean canExport = exportDAO.findByExporterAndAvailableIsTrue(SecurityUtils.getLoginUserAccount())
                .stream()
                .anyMatch(export -> Objects.equals(export.getChartId(), chartId));
        if (!canExport) {
            throw new ChartException("您並無此匯出權限");
        }
        Chart chart = chartDAO.findById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        List<Object[]> objects = dataSourceDAO.searchAll(chart.getDataSource());
        String sheetName = chart.getName();
        Workbook workbook = new PoiWorkbook(ExcelType.XLSX, sheetName);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = 0;
        for (Object[] object : objects) {
            Row row = sheet.getRowByRowIndex(rowCount);
            for (int j = 0; j < object.length; j++) {
                if (object[j] instanceof BigDecimal) {
                    // 如果是 BigDecimal，則設置為數值型
                    row.getCell(j).setValue(((BigDecimal) object[j]).doubleValue());
                } else if (object[j] instanceof String) {
                    // 如果是 String，則設置為字串型
                    row.getCell(j).setValue((String) object[j]);
                } else if (object[j] instanceof Boolean) {
                    // 如果是 Boolean，則設置為布林值
                    row.getCell(j).setValue((Boolean) object[j]);
                } else {
                    // 其他情況，視為字串來處理
                    row.getCell(j).setValue(object[j] != null ? object[j].toString() : "");
                }
            }
            rowCount++;
        }
        ExcelResponseUtils.response(response, workbook);
    }
}
