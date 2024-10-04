package tw.edu.ntub.imd.birc.sodd.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.service.DepartmentService;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.object.CollectionObjectData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import java.util.EnumSet;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/dropdown")
@PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
public class InfoDropdownController {
    private final DepartmentService departmentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "")
    public ResponseEntity<String> searchDropdown(@RequestParam(name = "type", required = false) String type,
                                                 HttpServletRequest request) {
        type = StringUtils.isNotBlank(type) ? type : "-1";
        ObjectData objectData = new ObjectData();
        switch (type) {
            case "identity":
                getIdentity(objectData);
                break;
            case "department":
                getDepartment(objectData);
                break;
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }

    private void getDepartment(ObjectData objectData) {
        CollectionObjectData departmentObjectData = objectData.createCollectionData();
        departmentObjectData.add("departmentList", departmentService.searchAll(),
                (contentData, bean) -> {
                    contentData.add("key", bean.getId());
                    contentData.add("value", bean.getName());
                });
    }

    private void getIdentity(ObjectData objectData) {
        CollectionObjectData identityObjectData = objectData.createCollectionData();
        EnumSet<Identity> resource = EnumSet.allOf(Identity.class);
        identityObjectData.add("identityList", resource,
                (contentData, bean) -> {
                    contentData.add("key", bean.getValue());
                    contentData.add("value", bean.getTypeName());
                });
    }
}
