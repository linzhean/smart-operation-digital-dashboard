package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.exception.ProjectException;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/chart")
public class ChartController {
    private final ChartService chartService;
    private final ChartDashboardService chartDashboardService;

    @PostMapping("/dashboard")
    public ResponseEntity<String> setChartInDashboard(@RequestParam("dashboardId") Integer dashboardId,
                                                      @RequestBody List<Integer> chartIds) {
        if (chartIds.isEmpty()) {
            List<Integer> originalIds = chartService.searchChartIdsByDashboardId(dashboardId);
            for (Integer chartId : chartIds) {
                if (!originalIds.contains(chartId)) {
                    chartDashboardService.save(chartId, dashboardId);
                }
                originalIds.remove(chartId);
            }
            originalIds.stream().forEach(id -> chartDashboardService.removeChartFromDashboard(id, dashboardId));
        }
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByDashboardId(@RequestParam("dashboardId") Integer dashboardId) {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchByDashboardId(dashboardId)) {
            String htmlContent = resourceToString(chartBean.getChart());
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("chart", htmlContent);
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    private String resourceToString(Resource resource) {
        InputStream inputStream = null;
        byte[] bytes;
        try {
            inputStream = resource.getInputStream();
            bytes = FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new ProjectException("Resource輸出成ByteArray錯誤") {
                @Override
                public String getErrorCode() {
                    return "Resource - Output Fail";
                }
            };
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @GetMapping("/all")
    public ResponseEntity<String> searchAll() {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchAll()) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("showcaseImage", chartBean.getShowcaseImage());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/available")
    public ResponseEntity<String> searchByUser() {
        ArrayData arrayData = new ArrayData();
        String userId = SecurityUtils.getLoginUserAccount();
        for (ChartBean chartBean : chartService.searchByUser(userId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("showcaseImage", chartBean.getShowcaseImage());
            objectData.add("observable", chartBean.getObservable());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/ai")
    public ResponseEntity<String> getChartSuggestion(@RequestParam("id") Integer id) throws IOException {
        ObjectData objectData = new ObjectData();
        objectData.add("suggestion", chartService.getChartSuggestion(id));
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }
}
