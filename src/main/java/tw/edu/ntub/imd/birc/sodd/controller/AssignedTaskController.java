package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskSponsorBean;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/assigned-task")
public class AssignedTaskController {
    private final AssignedTaskService taskService;
    private final AssignedTaskSponsorService sponsorService;

    @PostMapping("")
    public ResponseEntity<String> addAssignedTask(@RequestBody AssignedTaskBean assignedTaskBean,
                                                  BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        taskService.save(assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @PostMapping("/sponsor")
    public ResponseEntity<String> setSponsorInChart(@RequestParam("chartId") Integer chartId,
                                                    @RequestBody List<String> userIds) {
        if (userIds.isEmpty()) {
            List<String> originalIds = sponsorService.finByChartId(chartId)
                    .stream()
                    .map(sponsorBean -> sponsorBean.getSponsorUserId())
                    .collect(Collectors.toList());
            for (String userId : userIds) {
                if (!originalIds.contains(chartId)) {
                    sponsorService.save(chartId, userId);
                }
                originalIds.remove(chartId);
            }
            originalIds.stream().forEach(userId -> sponsorService.removeSponsorFromChart(userId, chartId));
        }
        return ResponseEntityBuilder.success()
                .message("設定成功")
                .build();
    }

    @GetMapping("/")
    public ResponseEntity<String> searchAll() {
        ArrayData arrayData = new ArrayData();
        for (AssignedTaskBean assignedTaskBean : taskService.searchAll()) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", assignedTaskBean.getId());
            objectData.add("chartId", assignedTaskBean.getChartId());
            objectData.add("name", assignedTaskBean.getName());
            objectData.add("defaultProcessor", assignedTaskBean.getDefaultProcessor());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/sponsor")
    public ResponseEntity<String> searchSponsorByChartId(@RequestParam("chartId") Integer chartId) {
        ArrayData arrayData = new ArrayData();
        for (AssignedTaskSponsorBean sponsorBean : sponsorService.finByChartId(chartId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("chartId", sponsorBean.getChartId());
            objectData.add("sponsorId", sponsorBean.getSponsorUserId());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String> patchAssignTask(@PathVariable("id") Integer id,
                                                  @RequestBody AssignedTaskBean assignedTaskBean) {
        taskService.update(id, assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delAssignTask(@PathVariable("id") Integer id) {
        AssignedTaskBean assignedTaskBean = new AssignedTaskBean();
        assignedTaskBean.setAvailable(false);
        taskService.update(id, assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
