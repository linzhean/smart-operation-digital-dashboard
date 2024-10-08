package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.exception.DataAlreadyExistException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.DepartmentService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.util.email.EmailUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.SingleValueObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/user-account")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final EmailUtils emailUtils;


    @GetMapping(path = "")
    public ResponseEntity<String> getLoginUser(HttpServletRequest request) {
        ObjectData objectData = new ObjectData();
        userAccountService
                .getById(SecurityUtils.getLoginUserAccount())
                .ifPresentOrElse(
                        userAccountBean -> {

                            String identity = SecurityUtils.getLoginUserIdentity();
                            identity = identity.substring(1, identity.length() - 1);

                            objectData.add("id", userAccountBean.getUserId());
                            objectData.add("name", userAccountBean.getUserName());
                            objectData.add("identity", identity);
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
            @PathVariable(name = "userId") String userId, HttpServletRequest request) {
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
            data.add("identity", identity.substring(1, identity.length() - 1));
        }
        data.add("department", departmentService.getDepartmentMap().getOrDefault(bean.getDepartmentId(), "查無此部門"));
        data.add("gmail", bean.getGmail());
        data.add("position", bean.getPosition());
        ArrayData arrayData = data.addArray("charAuths");
        for (GroupBean groupBean : groupService.searchByUserId(userId)) {
            ObjectData groupData = arrayData.addObject();
            groupData.add("groupId", groupBean.getId());
            groupData.add("groupName", groupBean.getName());
        }
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
            @RequestParam(name = "name", required = false) String name,
            HttpServletRequest request
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
            @RequestParam(name = "nowPage") Integer nowPage,
            HttpServletRequest request
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

    @PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
    @GetMapping(path = "/all")
    public ResponseEntity<String> searchAllUser(
            @RequestParam(name = "departmentId", required = false) String departmentId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity", required = false) String identity,
            HttpServletRequest request
    ) {
        ArrayData arrayData = new ArrayData();
        for (UserAccountBean bean : userAccountService.searchByUserValue(departmentId, name, identity)) {
            bean.setDepartmentName(departmentService.getDepartmentMap().getOrDefault(bean.getDepartmentId(), ""));
            ObjectData data = arrayData.addObject();
            data.add("userId", bean.getUserId());
            data.add("email", bean.getGmail());
            data.add("userName", bean.getUserName());
            data.add("departmentName", bean.getDepartmentName());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping(path = "")
    public ResponseEntity<String> updateUserValueByUserId(@RequestBody @Valid UserAccountBean userAccountBean,
                                                          BindingResult bindingResult,
                                                          HttpServletRequest request) {
        String identity = SecurityUtils.getLoginUserIdentity();
        boolean isManager = identity.equals(Identity.getIdentityName(Identity.ADMIN));
        if (!isManager && !SecurityUtils.getLoginUserAccount().equals(userAccountBean.getUserId())) {
            return ResponseEntityBuilder.error()
                    .errorCode("User - AccessDenied")
                    .message("您並無此操作之權限，請嘗試重新登入")
                    .build();
        }
        BindingResultUtils.validate(bindingResult);
        if (Identity.isNoPermission(identity)) {
            if (userAccountBean.getJobNumber() == null) {
                return ResponseEntityBuilder.error()
                        .errorCode("FormValidation - Invalid")
                        .message("員工編號 - 未填寫")
                        .build();
            }
        }
        try {
            Optional<UserAccountBean> accountBean = userAccountService.getById(userAccountBean.getJobNumber());
            if (accountBean.isPresent()) {
                throw new DataAlreadyExistException("此員工編號已經存在");
            }
        } catch (NotFoundException e) {
            userAccountBean.setUserId(userAccountBean.getJobNumber());
        }
        userAccountService.update(userAccountBean.getUserId(), userAccountBean);
        return ResponseEntityBuilder.success()
                .message("修改成功")
                .build();
    }


    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @PatchMapping("/admit")
    public ResponseEntity<String> admitUser(@RequestParam("userId") String userId,
                                            HttpServletRequest request) {
        UserAccountBean userAccountBean = userAccountService.getById(userId)
                .orElseThrow(() -> new NotFoundException("查無此使用者"));
        if (checkUserValueIsNull(userAccountBean)) {
            return ResponseEntityBuilder.success()
                    .message("使用者資料尚未填寫完畢，無法核准")
                    .build();
        }
        UserAccountBean bean = new UserAccountBean();
        String identity = userAccountBean.getIdentity().getTypeName();
        if (!Identity.isNoPermission(identity)) {
            return ResponseEntityBuilder.error()
                    .message("僅有無權限才可被開通")
                    .build();
        }
        bean.setIdentity(Identity.USER);
        userAccountService.update(userId, bean);
        emailUtils.sendMail(userId, userAccountBean.getGmail(), "審核通過通知",
                "src/main/resources/mail/adminPermitted.html", null);
        return ResponseEntityBuilder.success()
                .message("核准成功")
                .build();
    }

    private boolean checkUserValueIsNull(UserAccountBean userAccountBean) {
        return StringUtils.isBlank(userAccountBean.getUserName()) ||
                userAccountBean.getDepartmentId() == null ||
                StringUtils.isBlank(userAccountBean.getPosition());
    }

    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @PatchMapping("/able")
    public ResponseEntity<String> setUserAvailable(@RequestParam("userId") String userId,
                                                   HttpServletRequest request) {
        UserAccountBean userAccountBean = userAccountService.getById(userId)
                .orElseThrow(() -> new NotFoundException("查無此使用者"));
        userAccountBean.setAvailable(!userAccountBean.getAvailable());
        userAccountService.update(userId, userAccountBean);
        return ResponseEntityBuilder.success()
                .message(userAccountBean.getAvailable() ? "使用者已啟用" : "使用者已停用")
                .build();
    }

    @PreAuthorize(SecurityUtils.HAS_ADMIN_AUTHORITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delUserAccount(@PathVariable("id") String userId,
                                                 HttpServletRequest request) {
        userAccountService.delete(userId);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
