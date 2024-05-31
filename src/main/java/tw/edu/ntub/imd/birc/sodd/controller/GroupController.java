package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.service.DepartmentService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.UserGroupService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/group")
public class GroupController {
    private final GroupService groupService;
    private final UserGroupService userGroupService;
    private final DepartmentService departmentService;

    @PostMapping("")
    public ResponseEntity<String> addGroup(@RequestBody GroupBean groupBean, BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        groupService.save(groupBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUserToGroup(@RequestParam("userId") String userId,
                                                 @RequestParam("groupId") Integer groupId) {
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setUserId(userId);
        userGroupBean.setGroupId(groupId);
        userGroupService.save(userGroupBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchAll() {
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
                                                       @RequestParam(value = "position", required = false) String position) {
        ArrayData arrayData = new ArrayData();
        for (UserAccountBean userAccountBean : userGroupService
                .searchAllUserByGroupId(groupId, userName, department, position)) {
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
                                                 BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        groupService.update(id, groupBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> removeUserFromGroup(@RequestParam("userId") String userId,
                                                      @RequestParam("groupId") Integer groupId) {
        userGroupService.removeUserFromGroup(userId, groupId);
        return ResponseEntityBuilder.success()
                .message("移除成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delGroup(@PathVariable("id") Integer id) {
        GroupBean groupBean = new GroupBean();
        groupBean.setAvailable(false);
        groupService.update(id, groupBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
