package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.exception.ProjectException;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.dto.ListDTO;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.DashboardService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/chart")
public class ChartController {
    private final ChartService chartService;
    private final ChartDashboardService chartDashboardService;
    private final DashboardService dashboardService;

    @PostMapping("")
    public ResponseEntity<String> addChart(@RequestBody ChartBean chartBean,
                                           BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        chartService.save(chartBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @PostMapping("/dashboard")
    public ResponseEntity<String> setChartInDashboard(@RequestParam("dashboardId") Integer dashboardId,
                                                      @RequestBody ListDTO listDTO,
                                                      HttpServletRequest request) {
        dashboardService.getById(dashboardId)
                .orElseThrow(() -> new NotFoundException("查無此儀表板"));
        List<Integer> chartIds = listDTO.getDashboardCharts();
        if (!chartIds.isEmpty()) {
            Map<Integer, Integer> originals = chartDashboardService.findByDashboardId(dashboardId)
                    .stream()
                    .collect(Collectors.toMap(ChartDashboardBean::getChartId, ChartDashboardBean::getId));
            for (Integer chartId : chartIds) {
                chartService.getById(chartId)
                        .orElseThrow(() -> new NotFoundException("查無此圖表"));
                if (!originals.containsKey(chartId)) {
                    chartDashboardService.save(chartId, dashboardId);
                }
                originals.remove(chartId);
            }
            ChartDashboardBean chartDashboardBean = new ChartDashboardBean();
            chartDashboardBean.setAvailable(false);
            originals.forEach((chartId, id) -> chartDashboardService.update(id, chartDashboardBean));
        }
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByDashboardId(@RequestParam("dashboardId") Integer dashboardId,
                                                      HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchByDashboardId(dashboardId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("chartImage", chartBean.getChartImage());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        ObjectData objectData = new ObjectData();
        ChartBean chartBean = chartService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        objectData.add("name", chartBean.getName());
        objectData.add("chartHTML", resourceToString(chartBean.getChartHTML()));
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
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
    public ResponseEntity<String> searchAll(HttpServletRequest request) {
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
    public ResponseEntity<String> searchByUser(HttpServletRequest request) {
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
    public ResponseEntity<String> getChartSuggestion(@RequestParam("id") Integer id,
                                                     @RequestParam("dashboardId") Integer dashboardId,
                                                     HttpServletRequest request) throws IOException {
        ObjectData objectData = new ObjectData();
        objectData.add("suggestion", chartService.getChartSuggestion(id, dashboardId));
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }
}