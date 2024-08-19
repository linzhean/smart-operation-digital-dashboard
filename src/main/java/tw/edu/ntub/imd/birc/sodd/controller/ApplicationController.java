package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ApplicationBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ApplicationService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.SingleValueObjectData;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@RestController
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("")
    public ResponseEntity<String> addApplication(@RequestBody ApplicationBean applicationBean,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        applicationService.save(applicationBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchApplication(@RequestParam("status") String status,
                                                    @RequestParam(value = "startDate", required = false) String startDate,
                                                    @RequestParam(value = "endDate", required = false) String endDate,
                                                    @RequestParam("nowPage") Integer nowPage,
                                                    HttpServletRequest request) {
        String userId = SecurityUtils.getLoginUserAccount();
        String identity = SecurityUtils.getLoginUserIdentity();
        ArrayData arrayData = new ArrayData();
        for (ApplicationBean applicationBean :
                applicationService.searchApplication(userId, identity, status, startDate, endDate, nowPage)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", applicationBean.getId());
            objectData.add("chartId", applicationBean.getChartId());
            objectData.add("applicant", applicationBean.getApplicant());
            objectData.add("guarantor", applicationBean.getGuarantor());
            objectData.add("startDate", applicationBean.getStartDate());
            objectData.add("endDate", applicationBean.getEndDate());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/page")
    public ResponseEntity<String> countApplication(@RequestParam("status") String status,
                                                   @RequestParam(value = "startDate", required = false) String startDate,
                                                   @RequestParam(value = "endDate", required = false) String endDate,
                                                   HttpServletRequest request) {
        String userId = SecurityUtils.getLoginUserAccount();
        String identity = SecurityUtils.getLoginUserIdentity();
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(SingleValueObjectData.create("totalPage",
                        applicationService.countApplication(userId, identity, status, startDate, endDate)))
                .build();
    }

    @PatchMapping("/permit/{id}")
    public ResponseEntity<String> permitApplication(@PathVariable("id") Integer id,
                                                    HttpServletRequest request) {
        String userId = SecurityUtils.getLoginUserAccount();
        ApplicationBean applicationBean = applicationService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此申請"));
        if (!Apply.isNotPassed(applicationBean.getApplyStatus().getApplyStatus())) {
            return ResponseEntityBuilder.error()
                    .message("申請的狀態不為可被允許通過的狀態")
                    .build();
        }
        applicationService.permitApplication(applicationBean, userId);
        return ResponseEntityBuilder.success()
                .message("申請允許通過成功")
                .build();
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<String> closeApplication(@PathVariable("id") Integer id,
                                                   HttpServletRequest request) {
        ApplicationBean applicationBean = applicationService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此申請"));
        if (Apply.isClosed(applicationBean.getApplyStatus().getApplyStatus())) {
            return ResponseEntityBuilder.success()
                    .message("此申請已關閉")
                    .build();
        }
        applicationService.close(id, applicationBean);
        return ResponseEntityBuilder.success()
                .message("申請關閉成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delApplication(@PathVariable("id") Integer id,
                                                 HttpServletRequest request) {
        ApplicationBean applicationBean = applicationService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此申請"));
        if (!Apply.isClosed(applicationBean.getApplyStatus().getApplyStatus())) {
            return ResponseEntityBuilder.success()
                    .message("申請必須為已關閉才可被刪除")
                    .build();
        }
        applicationBean.setAvailable(false);
        applicationService.update(id, applicationBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
