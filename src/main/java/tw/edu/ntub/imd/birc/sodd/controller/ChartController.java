package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@RestController
@RequestMapping("/chart")
public class ChartController {
    private final ChartService chartService;

    @GetMapping("")
    public ResponseEntity<String> searchByDashboardId(@RequestParam("dashboardId") Integer dashboardId) throws IOException {
        String userId = SecurityUtils.getLoginUserAccount();
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartService.searchByDashboardId(dashboardId)) {
            InputStream inputStream = chartBean.getChart().getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            String htmlContent = new String(bytes, StandardCharsets.UTF_8);
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
}
