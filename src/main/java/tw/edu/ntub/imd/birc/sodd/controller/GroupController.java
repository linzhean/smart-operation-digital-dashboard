package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.*;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartGroupId;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroupId;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.*;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/group")
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
        UserGroupId userGroupId = new UserGroupId(userId, groupId);
        Optional<UserGroupBean> userGroupOptional = userGroupService.getById(userGroupId);
        if (userGroupOptional.isPresent()) {
            return ResponseEntityBuilder.success()
                    .message("您已經新增" + userId + "至群組了")
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
        ChartBean chartBean = chartService.getById(chartId)
                .orElseThrow(() -> new NotFoundException("查無此圖表: " + chartId));
        groupService.getById(groupId)
                .orElseThrow(() -> new NotFoundException("查無此群組: " + groupId));
        ChartGroupId chartGroupId = new ChartGroupId(chartId, groupId);
        Optional<ChartGroupBean> chartGroupOptional = chartGroupService.getById(chartGroupId);
        if (chartGroupOptional.isPresent()) {
            return ResponseEntityBuilder.success()
                    .message("您已經新增" + chartBean.getName() + "至群組了")
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

    @GetMapping("")
    public ResponseEntity<String> searchAll(HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (GroupBean groupBean : groupService.searchAll()) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", groupBean.getId());
            objectData.add("name", groupBean.getName());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<String> searchUsersByGroupId(@PathVariable("groupId") Integer groupId,
                                                       @RequestParam(value = "userName", required = false) String userName,
                                                       @RequestParam(value = "department", required = false) String department,
                                                       @RequestParam(value = "position", required = false) String position,
                                                       HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (UserAccountBean userAccountBean : userGroupService
                .searchUserByGroupId(groupId, userName, department, position)) {
            ObjectData objectData = arrayData.addObject();
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
    public ResponseEntity<String> removeUserFromGroup(@RequestParam("userId") String userId,
                                                      @RequestParam("groupId") Integer groupId,
                                                      HttpServletRequest request) {
        userGroupService.removeUserFromGroup(userId, groupId);
        return ResponseEntityBuilder.success()
                .message("移除成功")
                .build();
    }

    @DeleteMapping("/chart")
    public ResponseEntity<String> removeChartFromGroup(@RequestParam("chartId") Integer chartId,
                                                       @RequestParam("groupId") Integer groupId,
                                                       HttpServletRequest request) {
        chartGroupService.removeChartFromGroup(chartId, groupId);
        return ResponseEntityBuilder.success()
                .message("移除成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delGroup(@PathVariable("id") Integer id, HttpServletRequest request) {
        GroupBean groupBean = new GroupBean();
        groupBean.setAvailable(false);
        groupService.update(id, groupBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
