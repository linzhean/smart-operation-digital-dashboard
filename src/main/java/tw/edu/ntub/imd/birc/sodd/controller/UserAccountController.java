package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.DepartmentService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.SingleValueObjectData;

@AllArgsConstructor
@RestController
@RequestMapping("/user-account")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final GroupService groupService;
    private final DepartmentService departmentService;

    @GetMapping(path = "")
    public ResponseEntity<String> getLoginUser() {
        ObjectData objectData = new ObjectData();
        userAccountService
                .getById(SecurityUtils.getLoginUserAccount())
                .ifPresentOrElse(
                        userAccountBean -> {
                            objectData.add("id", userAccountBean.getUserId());
                            objectData.add("name", userAccountBean.getUserName());

                            ArrayData arrayData = objectData.addArray("authentications");
                            for (GroupBean groupBean : groupService.searchByUserId(userAccountBean.getUserId())) {
                                ObjectData groupData = arrayData.addObject();
                                groupData.add("groupId", groupBean.getId());
                                groupData.add("groupName", groupBean.getName());
                            }
                        },
                        () -> objectData.add("name", ""));
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<String> getByUserId(
            @PathVariable(name = "userId") String userId) {
        String identity = SecurityUtils.getLoginUserIdentity();
        boolean isAdmin = identity.equals(Identity.getIdentityName(Identity.ADMIN));
        if (!isAdmin && !SecurityUtils.getLoginUserAccount().equals(userId)) {
            return ResponseEntityBuilder.error()
                    .errorCode("User - AccessDenied")
                    .message("您並無此操作之權限，請嘗試重新登入")
                    .build();
        }
        UserAccountBean bean = userAccountService.getById(userId)
                .orElseThrow(() -> new NotFoundException("找不到此用戶：" + userId));
        ObjectData data = new ObjectData();
        data.add("userName", bean.getUserName());
        if (isAdmin) {
            data.add("identity", Identity.getIdentityName(bean.getIdentity()));
        }
        data.add("department", bean.getDepartmentId());

        boolean isSelf = SecurityUtils.getLoginUserAccount().equals(userId) && bean.getCreateDate().equals(bean.getModifyDate());
        data.add("isWriter", isAdmin || isSelf);
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(data)
                .build();
    }

    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @GetMapping(path = "/page")
    public ResponseEntity<String> countUserList(
            @RequestParam(name = "departmentId", required = false) String departmentId,
            @RequestParam(name = "identity", required = false) String identity,
            @RequestParam(name = "name", required = false) String name
    ) {
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(SingleValueObjectData.create("totalPage", userAccountService.countUserList(departmentId, identity, name)))
                .build();
    }

    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @GetMapping(path = "/list", params = "nowPage")
    public ResponseEntity<String> searchUserList(
            @RequestParam(name = "departmentId", required = false) String departmentId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity", required = false) String identity,
            @RequestParam(name = "nowPage") Integer nowPage
    ) {
        ArrayData arrayData = new ArrayData();
        for (UserAccountBean bean : userAccountService.searchByUserValue(departmentId, name, identity, nowPage)) {
            bean.setDepartmentName(departmentService.getDepartmentMap().getOrDefault(bean.getDepartmentId(), ""));
            ObjectData data = arrayData.addObject();
            data.add("userId", bean.getUserId());
            data.add("email", bean.getGmail());
            data.add("userName", bean.getUserName());
            data.add("departmentName", bean.getDepartmentName());
            data.add("identity", bean.getIdentity().getTypeName());
            data.add("available", bean.getAvailable());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping(path = "")
    public ResponseEntity<String> updateUserValueByUserId(@RequestBody UserAccountBean userAccountBean) {
        String identity = SecurityUtils.getLoginUserIdentity();
        boolean isManager = identity.equals(Identity.getIdentityName(Identity.ADMIN));
        if (!isManager && !SecurityUtils.getLoginUserAccount().equals(userAccountBean.getUserId())) {
            return ResponseEntityBuilder.error()
                    .errorCode("User - AccessDenied")
                    .message("您並無此操作之權限，請嘗試重新登入")
                    .build();
        }

        if (StringUtils.isNotBlank(userAccountBean.getIdentity().getTypeName())) {
            userAccountBean.setIdentity(userAccountBean.getIdentity());
        }
        userAccountService.update(userAccountBean.getUserId(), userAccountBean);
        return ResponseEntityBuilder.success()
                .message("修改成功")
                .build();
    }

    @PatchMapping("/admit")
    public ResponseEntity<String> admitUser(@RequestParam("userId") String userId,
                                            @RequestParam("identity") String identity) {
        UserAccountBean userAccountBean = userAccountService.getById(userId)
                .orElseThrow(() -> new NotFoundException("查無此使用者"));
        if (checkUserValueIsNull(userAccountBean)) {
            return ResponseEntityBuilder.success()
                    .message("使用者資料尚未填寫完畢，無法核准")
                    .build();
        }
        UserAccountBean bean = new UserAccountBean();
        bean.setIdentity(Identity.of(identity));
        userAccountService.update(userId, bean);
        return ResponseEntityBuilder.success()
                .message("核准成功")
                .build();
    }

    private boolean checkUserValueIsNull(UserAccountBean userAccountBean) {
        return StringUtils.isBlank(userAccountBean.getUserName()) &&
                userAccountBean.getDepartmentId() == null &&
                StringUtils.isBlank(userAccountBean.getPosition());
    }
}
