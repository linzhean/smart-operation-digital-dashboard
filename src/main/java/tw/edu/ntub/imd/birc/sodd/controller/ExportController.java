package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.ExportBean;
import tw.edu.ntub.imd.birc.sodd.service.ExportService;
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

    @PostMapping("")
    public ResponseEntity<String> setExporterInChart(@RequestParam("chartId") Integer chartId,
                                                     @RequestBody List<String> userIds,
                                                     HttpServletRequest request) {
        if (userIds.isEmpty()) {
            Map<String, Integer> originals = exportService.findByChartId(chartId)
                    .stream()
                    .collect(Collectors.toMap(ExportBean::getExporter, ExportBean::getId));
            for (String userId : userIds) {
                if (!originals.containsKey(userId)) {
                    exportService.save(chartId, userId);
                }
                originals.remove(userId);
            }
            ExportBean exportBean = new ExportBean();
            exportBean.setAvailable(false);
            originals.forEach((userId, id) -> exportService.update(id, exportBean));
        }
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }

    @PostMapping("/export")
    public void exportExcel(@RequestParam("chartId") Integer chartId,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        exportService.export(chartId, response);
    }

    @GetMapping("")
    public ResponseEntity<String> searchByChartId(@RequestParam("chartId") Integer chartId,
                                                  HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ExportBean exportBean : exportService.findByChartId(chartId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", exportBean.getId());
            objectData.add("chartId", exportBean.getChartId());
            objectData.add("exporter", exportBean.getExporter());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .build();
    }
}

