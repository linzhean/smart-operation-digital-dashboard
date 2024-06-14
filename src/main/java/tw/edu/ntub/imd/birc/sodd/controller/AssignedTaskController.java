package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/assigned-task")
public class AssignedTaskController {
    private final AssignedTaskService taskService;
    private final AssignedTaskSponsorService sponsorService;

    @PostMapping("")
    public ResponseEntity<String> addAssignedTask(@RequestBody AssignedTaskBean assignedTaskBean, BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        taskService.save(assignedTaskBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }
}
