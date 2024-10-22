package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.*;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.*;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/group")
@PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
public class GroupController {
    private final GroupService groupService;
    private final UserAccountService userAccountService;
    private final UserGroupService userGroupService;
    private final ChartService chartService;
    private final ChartGroupService chartGroupService;
    private final DepartmentService departmentService;

    @PostMapping("")
    public ResponseEntity<String> addGroup(@RequestBody GroupBean groupBean,
                                           BindingResult bindingResult,
                                           HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        groupService.save(groupBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUserToGroup(@RequestParam("userId") String userId,
                                                 @RequestParam("groupId") Integer groupId,
                                                 HttpServletRequest request) {
        userAccountService.getById(userId)
                .orElseThrow(() -> new NotFoundException("查無此使用者: " + userId));
        groupService.getById(groupId)
                .orElseThrow(() -> new NotFoundException("查無此群組: " + groupId));
        boolean isAdded = userGroupService.searchUserByGroupId(groupId)
                .stream()
                .anyMatch(userAccountBean -> Objects.equals(userAccountBean.getUserId(), userId));
        if (isAdded) {
            return ResponseEntityBuilder.success()
                    .message("此用戶已被新增至群組")
                    .build();
        }
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setUserId(userId);
        userGroupBean.setGroupId(groupId);
        userGroupService.save(userGroupBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @PostMapping("/chart")
    public ResponseEntity<String> addChartToGroup(@RequestParam("groupId") Integer groupId,
                                                  @RequestParam("chartId") Integer chartId,
                                                  HttpServletRequest request) {
        chartService.getById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此圖表: " + chartId));
        groupService.getById(groupId)
                .orElseThrow(() -> new NotFoundException("查無此群組: " + groupId));
        boolean isAdded = chartGroupService.searchChartByGroupId(groupId)
                .stream()
                .anyMatch(chartBean -> Objects.equals(chartBean.getId(), chartId));
        if (isAdded) {
            return ResponseEntityBuilder.success()
                    .message("此圖表已被新增到群組了")
                    .build();
        }
        ChartGroupBean chartGroupBean = new ChartGroupBean();
        chartGroupBean.setGroupId(groupId);
        chartGroupBean.setChartId(chartId);
        chartGroupService.save(chartGroupBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<String> searchAll(HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (GroupBean groupBean : groupService.searchAll()) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", groupBean.getId());
            objectData.add("name", groupBean.getName());
            objectData.add("userCounts", userGroupService.searchUserByGroupId(groupBean.getId()).size());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/user/{groupId}")
    public ResponseEntity<String> searchUsersByGroupId(@PathVariable("groupId") Integer groupId,
                                                       @RequestParam(value = "userName", required = false) String userName,
                                                       @RequestParam(value = "department", required = false) String department,
                                                       @RequestParam(value = "position", required = false) String position,
                                                       HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (UserAccountBean userAccountBean : userGroupService
                .searchUserByGroupId(groupId, userName, department, position)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("userGroupId", userAccountBean.getUserGroupId());
            objectData.add("userId", userAccountBean.getUserId());
            objectData.add("userName", userAccountBean.getUserName());
            objectData.add("department", departmentService.getDepartmentMap()
                    .getOrDefault(userAccountBean.getDepartmentId(), "查無此部門"));
            objectData.add("gmail", userAccountBean.getGmail());
            objectData.add("identity", userAccountBean.getIdentity().getTypeName());
            objectData.add("position", userAccountBean.getPosition());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/chart/{groupId}")
    public ResponseEntity<String> searchChartsByGroupId(@PathVariable("groupId") Integer groupId,
                                                        HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (ChartBean chartBean : chartGroupService
                .searchChartByGroupId(groupId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("chartId", chartBean.getId());
            objectData.add("chartName", chartBean.getName());
            objectData.add("showcaseImage", chartBean.getShowcaseImage());
            objectData.add("chartGroupId", chartBean.getChartGroupId());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchGroupName(@PathVariable("id") Integer id,
                                                 @RequestBody GroupBean groupBean,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        groupService.update(id, groupBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> removeUserFromGroup(@RequestParam("userGroupId") Integer userGroupId,
                                                      HttpServletRequest request) {
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setAvailable(false);
        userGroupService.update(userGroupId, userGroupBean);
        return ResponseEntityBuilder.success()
                .message("移除成功")
                .build();
    }

    @DeleteMapping("/chart")
    public ResponseEntity<String> removeChartFromGroup(@RequestParam("userGroupId") Integer chartGroupId,
                                                       HttpServletRequest request) {
        ChartGroupBean chartGroupBean = new ChartGroupBean();
        chartGroupBean.setAvailable(false);
        chartGroupService.update(chartGroupId, chartGroupBean);
        return ResponseEntityBuilder.success()
                .message("移除成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delGroup(@PathVariable("id") Integer id, HttpServletRequest request) {
        groupService.delGroup(id);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
