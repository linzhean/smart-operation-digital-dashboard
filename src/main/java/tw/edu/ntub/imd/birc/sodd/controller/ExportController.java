package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.dto.ListDTO;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.ExportService;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/export")
public class ExportController {
    private final ExportService exportService;
    private final UserAccountService userAccountService;
    private final ChartService chartService;


    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @PostMapping("")
    public ResponseEntity<String> setExporterInChart(@RequestParam("chartId") Integer chartId,
                                                     @RequestBody ListDTO listDTO,
                                                     HttpServletRequest request) {
        chartService.getById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        List<String> userIds = listDTO.getExporterList();
        Map<String, Integer> originals = exportService.findByChartId(chartId)
                .stream()
                .collect(Collectors.toMap(ExportBean::getExporter, ExportBean::getId));
        for (String userId : userIds) {
            userAccountService.getById(userId)
                    .orElseThrow(() -> new NotFoundException("查無此使用者"));
            if (!originals.containsKey(userId)) {
                exportService.save(chartId, userId);
            }
            originals.remove(userId);
        }
        ExportBean exportBean = new ExportBean();
        exportBean.setAvailable(false);
        originals.forEach((userId, id) -> exportService.update(id, exportBean));
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }

    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @PostMapping("/export")
    public void exportExcel(@RequestParam("chartId") Integer chartId,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        exportService.export(chartId, response);
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping("")
    public ResponseEntity<String> searchByChartId(@RequestParam("chartId") Integer chartId,
                                                  HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ExportBean exportBean : exportService.findByChartId(chartId)) {
            String exporterName = userAccountService.getById(exportBean.getExporter())
                    .map(UserAccountBean::getUserName)
                    .orElse("");
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", exportBean.getId());
            objectData.add("chartId", exportBean.getChartId());
            objectData.add("exporter", exportBean.getExporter());
            objectData.add("exporterName", exporterName);
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }
}

