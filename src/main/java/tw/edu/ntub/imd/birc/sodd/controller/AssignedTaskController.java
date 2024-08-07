package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.*;
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

    @PostMapping("")
    public ResponseEntity<String> addAssignedTask(@Valid @RequestBody AssignedTaskBean assignedTaskBean,
                                                  BindingResult bindingResult,
                                                  HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        taskService.save(assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

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

    @GetMapping("")
    public ResponseEntity<String> searchAll(HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (AssignedTaskBean assignedTaskBean : taskService.searchAll()) {
            String chartName = chartService.getById(assignedTaskBean.getChartId())
                    .map(ChartBean::getName)
                    .orElse("");
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", assignedTaskBean.getId());
            objectData.add("chartId", assignedTaskBean.getChartId());
            objectData.add("chartName", chartName);
            objectData.add("name", assignedTaskBean.getName());
            objectData.add("defaultProcessor", assignedTaskBean.getDefaultProcessor());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

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


    @PatchMapping("/{id}")
    public ResponseEntity<String> patchAssignTask(@PathVariable("id") Integer id,
                                                  @RequestBody AssignedTaskBean assignedTaskBean,
                                                  HttpServletRequest request) {
        taskService.update(id, assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delAssignTask(@PathVariable("id") Integer id,
                                                HttpServletRequest request) {
        AssignedTaskBean assignedTaskBean = new AssignedTaskBean();
        assignedTaskBean.setAvailable(false);
        taskService.update(id, assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
