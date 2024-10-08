package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.*;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.dto.ListDTO;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/assigned-task")
public class AssignedTaskController {
    private final AssignedTaskService taskService;
    private final AssignedTaskSponsorService sponsorService;
    private final ChartService chartService;
    private final UserAccountService userAccountService;


    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @PostMapping("/sponsor")
    public ResponseEntity<String> setSponsorInChart(@RequestParam("chartId") Integer chartId,
                                                    @RequestBody ListDTO listDTO,
                                                    HttpServletRequest request) {
        chartService.getById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        List<String> userIds = listDTO.getSponsorList();
        if (!userIds.isEmpty()) {
            Map<String, Integer> originalIds = sponsorService.findByChartId(chartId)
                    .stream()
                    .collect(Collectors.toMap(AssignedTaskSponsorBean::getSponsorUserId, AssignedTaskSponsorBean::getId));
            for (String userId : userIds) {
                userAccountService.getById(userId)
                        .orElseThrow(() -> new NotFoundException("查無此使用者"));
                if (!originalIds.containsKey(userId)) {
                    sponsorService.save(chartId, userId);
                }
                originalIds.remove(userId);
            }
            AssignedTaskSponsorBean sponsorBean = new AssignedTaskSponsorBean();
            sponsorBean.setAvailable(false);
            originalIds.forEach((userId, id) -> sponsorService.update(id, sponsorBean));
        }
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }


    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @GetMapping("")
    public ResponseEntity<String> getById(@RequestParam("chartId") Integer chartId,
                                          HttpServletRequest request) {
        ObjectData objectData = new ObjectData();
        AssignedTaskBean assignedTaskBean = taskService.getById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此交辦事項"));
        String chartName = chartService.getById(assignedTaskBean.getChartId())
                .map(ChartBean::getName)
                .orElse("");
        objectData.add("chartId", assignedTaskBean.getChartId());
        objectData.add("chartName", chartName);
        objectData.add("defaultAuditor", assignedTaskBean.getDefaultAuditor());
        objectData.add("defaultProcessor", assignedTaskBean.getDefaultProcessor());
        objectData.add("upperLimit", assignedTaskBean.getUpperLimit());
        objectData.add("lowerLimit", assignedTaskBean.getLowerLimit());
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }


    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping("/sponsor")
    public ResponseEntity<String> searchSponsorByChartId(@RequestParam("chartId") Integer chartId,
                                                         HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (AssignedTaskSponsorBean sponsorBean : sponsorService.findByChartId(chartId)) {
            String sponsorName = userAccountService.getById(sponsorBean.getSponsorUserId())
                    .map(UserAccountBean::getUserName)
                    .orElse("");
            ObjectData objectData = arrayData.addObject();
            objectData.add("chartId", sponsorBean.getChartId());
            objectData.add("sponsorId", sponsorBean.getSponsorUserId());
            objectData.add("sponsorName", sponsorName);
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchAssignTask(@PathVariable("id") Integer id,
                                                  @RequestBody AssignedTaskBean assignedTaskBean,
                                                  BindingResult bindingResult,
                                                  HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        String errorMessage = checkLimits(assignedTaskBean);
        if (StringUtils.isNotBlank(errorMessage)) {
            return ResponseEntityBuilder.success()
                    .message(errorMessage)
                    .build();
        }
        taskService.update(id, assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    private String checkLimits(AssignedTaskBean assignedTaskBean) {
        final double spacing = 0.1;
        String message = null;
        if (assignedTaskBean.getUpperLimit() - assignedTaskBean.getLowerLimit() < spacing) {
            message = "上下限間距不可超過" + spacing;
        }
        if (assignedTaskBean.getUpperLimit() <= assignedTaskBean.getLowerLimit()) {
            message = "上限不可小於等於下限";
        }
        return message;
    }
}
