package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.edu.ntub.birc.common.exception.ProjectException;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.dto.ListDTO;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.MultipartFileUploader;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.UploadResult;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.DashboardService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private final MultipartFileUploader multipartFileUploader;

    @PreAuthorize(SecurityUtils.HAS_DEVELOPER_AUTHORITY)
    @PostMapping("")
    public ResponseEntity<String> addChart(@Valid ChartBean chartBean,
                                           BindingResult bindingResult,
                                           HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        chartService.save(chartBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
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


    @PreAuthorize(SecurityUtils.HAS_DEVELOPER_AUTHORITY)
    @GetMapping("")
    public ResponseEntity<String> searchByAvailable(@RequestParam("available") Boolean available,
                                                    HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchByAvailable(available)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("available", chartBean.getAvailable());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping("/dashboard")
    public ResponseEntity<String> searchByDashboardId(@RequestParam("dashboardId") Integer dashboardId,
                                                      HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchByDashboardId(dashboardId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("chartImage", chartBean.getChartImage());
            objectData.add("canAssign", chartBean.getCanAssign());
            objectData.add("canExport", chartBean.getCanExport());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id,
                                          HttpServletRequest request) {
        ObjectData objectData = new ObjectData();
        ChartBean chartBean = chartService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        objectData.add("name", chartBean.getName());
        objectData.add("chartHTML", chartService.genChartHTML(chartBean));
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
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


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping("/available")
    public ResponseEntity<String> searchByUser(@RequestParam(value = "dashboardId", required = false) Integer dashboardId,
                                               HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        String userId = SecurityUtils.getLoginUserAccount();
        for (ChartBean chartBean : chartService.searchByUser(userId, dashboardId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", chartBean.getId());
            objectData.add("name", chartBean.getName());
            objectData.add("showcaseImage", chartBean.getShowcaseImage());
            objectData.add("observable", chartBean.getObservable());
            if (dashboardId != null) {
                objectData.add("isAdded", chartBean.getIsAdded());
            }
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize(SecurityUtils.HAS_DEVELOPER_AUTHORITY)
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchChartAvailable(@PathVariable("id") Integer id,
                                                      HttpServletRequest request) {
        ChartBean chartBean = chartService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        chartBean.setAvailable(!chartBean.getAvailable());
        if (!chartBean.getAvailable()) {
            for (ChartDashboardBean chartDashboardBean : chartDashboardService.findByChartId(id)) {
                chartDashboardBean.setAvailable(false);
                chartDashboardService.update(chartDashboardBean.getId(), chartDashboardBean);
            }
        }
        chartService.update(id, chartBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }


    /**
     * 更新圖表示意圖用
     */
//    @PreAuthorize(SecurityUtils.HAS_DEVELOPER_AUTHORITY)
//    @PatchMapping("/showcaseImage")
//    public ResponseEntity<String> uploadShowCaseImage(@RequestParam("chartId") Integer chartId,
//                                                      @RequestPart("file") MultipartFile multipartFile) {
//        ChartBean chartBean = chartService.getById(chartId)
//                .orElseThrow(() -> new NotFoundException(""));
//        UploadResult uploadResult = multipartFileUploader.upload(multipartFile, "showcaseImage", chartBean.getName());
//        chartBean.setShowcaseImage(uploadResult.getUrl());
//        chartService.update(chartId, chartBean);
//        return ResponseEntityBuilder.success()
//                .message("更新成功")
//                .build();
//    }
}