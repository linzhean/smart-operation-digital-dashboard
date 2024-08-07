package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.DashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.service.ChartDashboardService;
import tw.edu.ntub.imd.birc.sodd.service.DashboardService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @PostMapping("")
    public ResponseEntity<String> addDashboard(@RequestBody DashboardBean dashboardBean,
                                               BindingResult bindingResult,
                                               HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        dashboardService.save(dashboardBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByUser(HttpServletRequest request) {
        String userId = SecurityUtils.getLoginUserAccount();
        ArrayData arrayData = new ArrayData();
        for (DashboardBean dashboardBean : dashboardService.searchByUser(userId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", dashboardBean.getId());
            objectData.add("name", dashboardBean.getName());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchDashboard(@PathVariable("id") Integer id,
                                                 @RequestBody DashboardBean dashboardBean,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        dashboardService.update(id, dashboardBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delDashboard(@PathVariable("id") Integer id, HttpServletRequest request) {
        DashboardBean dashboardBean = new DashboardBean();
        dashboardBean.setAvailable(false);
        dashboardService.update(id, dashboardBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
