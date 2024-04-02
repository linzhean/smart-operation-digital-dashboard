package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping("")
    public ResponseEntity<String> addGroup(@RequestParam("groupName") String groupName, BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        GroupBean groupBean = new GroupBean();
        groupBean.setName(groupName);
        groupService.save(groupBean);
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
}
