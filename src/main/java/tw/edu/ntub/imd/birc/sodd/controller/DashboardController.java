package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.DashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.DashboardService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

@AllArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @PostMapping("")
    public ResponseEntity<String> addDashboard(@RequestBody DashboardBean dashboardBean, BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        dashboardService.save(dashboardBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByUser() {
        String userId = SecurityUtils.getLoginUserAccount();
        ArrayData arrayData = new ArrayData();
        for (DashboardBean dashboardBean : dashboardService.searchByUser(userId)) {
            ObjectData objectData = new ObjectData();
            objectData.add("id", dashboardBean.getId());
            objectData.add("name", dashboardBean.getName());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        ObjectData objectData = new ObjectData();
        DashboardBean dashboardBean = dashboardService.findById(id)
                .orElseThrow(() -> new NotFoundException("查無此儀表板"));
        objectData.add("id", dashboardBean.getId());
        objectData.add("name", dashboardBean.getName());

    }

}
