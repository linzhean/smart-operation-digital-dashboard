package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.AiChatBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.AIGenType;
import tw.edu.ntub.imd.birc.sodd.service.AiChatService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.SingleValueObjectData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/ai")
@PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
public class AiChatController {
    private final AiChatService aiChatService;

    @PostMapping("/chat")
    public ResponseEntity<String> addChat(@RequestBody AiChatBean aiChatBean,
                                          BindingResult bindingResult,
                                          HttpServletRequest request) throws IOException {
        BindingResultUtils.validate(bindingResult);
        aiChatBean.setGenerator(AIGenType.USER);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .data(SingleValueObjectData.create("newChat", aiChatService.getNewChat(aiChatBean)))
                .build();
    }


    @GetMapping("/suggestion")
    public ResponseEntity<String> getChartSuggestion(@RequestParam("chartId") Integer chartId,
                                                     @RequestParam("dashboardId") Integer dashboardId,
                                                     HttpServletRequest request) throws Exception {
        String suggestion = aiChatService.getChartSuggestion(chartId, dashboardId);
        AiChatBean aiChatBean = new AiChatBean();
        aiChatBean.setChartId(chartId);
        aiChatBean.setContent(suggestion);
        aiChatBean.setGenerator(AIGenType.AI);
        aiChatBean = aiChatService.save(aiChatBean);
        ObjectData objectData = new ObjectData();
        objectData.add("id", aiChatBean.getId());
        objectData.add("suggestion", suggestion);
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByChartId(@RequestParam("chartId") Integer chartId,
                                                  HttpServletRequest request) {
        ArrayData arrayData = new ArrayData();
        for (AiChatBean aiChatBean : aiChatService.searchByChartId(chartId)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", aiChatBean.getId());
            objectData.add("chartId", aiChatBean.getChartId());
            objectData.add("messageId", aiChatBean.getMessageId());
            objectData.add("content", aiChatBean.getContent());
            objectData.add("generator", aiChatBean.getGenerator().getType());
            objectData.add("createDate", aiChatBean.getCreateDate());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @DeleteMapping("")
    public ResponseEntity<String> delAiChat(@RequestParam("chartId") Integer chartId,
                                            HttpServletRequest request) {
        List<AiChatBean> aiChatBeans = aiChatService.searchByChartId(chartId);
        aiChatBeans.forEach(aiChatBean -> {
            aiChatBean.setAvailable(false);
            aiChatService.update(aiChatBean.getId(), aiChatBean);
        });
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
