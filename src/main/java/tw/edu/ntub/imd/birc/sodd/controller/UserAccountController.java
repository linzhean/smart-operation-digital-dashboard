package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

@AllArgsConstructor
@RestController
@RequestMapping("/user-account")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final GroupService groupService;

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
}
