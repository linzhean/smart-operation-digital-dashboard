package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.*;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;
    private final MailMessageService messageService;
    private final ChartService chartService;
    private final AssignedTaskService assignedTaskService;

    @PostMapping("")
    public ResponseEntity<String> sendAssignMail(@RequestBody MailBean mailBean,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        if (StringUtils.isBlank(mailBean.getFirstMessage().getContent())) {
            return ResponseEntityBuilder.error()
                    .message("郵件訊息 - 未填寫")
                    .build();
        }
        mailService.save(mailBean);
        return ResponseEntityBuilder.success()
                .message("交辦發送成功")
                .build();
    }

    @PostMapping("/message")
    public ResponseEntity<String> addMessage(@RequestParam("mailId") Integer mailId,
                                             @Valid @RequestBody MailMessageBean mailMessageBean,
                                             BindingResult bindingResult,
                                             HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        mailMessageBean.setMailId(mailId);
        messageService.save(mailMessageBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByStatus(@RequestParam("status") String status,
                                                 HttpServletRequest request) {
        String userId = SecurityUtils.getLoginUserAccount();
        ArrayData arrayData = new ArrayData();
        for (MailBean mailBean : mailService.searchByStatus(userId, status)) {
            String chartName = chartService.getById(mailBean.getChartId())
                    .map(ChartBean::getName)
                    .orElseThrow(() -> new NotFoundException("查無此圖表"));
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", mailBean.getId());
            objectData.add("chartName", chartName);
            objectData.add("name", mailBean.getName());
            objectData.add("status", ProcessStatus.getProcessStatusName(mailBean.getStatus()));
            objectData.add("publisher", mailBean.getPublisher());
            objectData.add("receiver", mailBean.getReceiver());
            objectData.add("emailSendTime", mailBean.getEmailSendTime());
            if (mailBean.getAssignedTaskId() != null) {
                String assignedTaskName = assignedTaskService.getById(mailBean.getAssignedTaskId())
                        .map(AssignedTaskBean::getName)
                        .orElseThrow(() -> new NotFoundException("查無此交辦"));
                objectData.add("assignedTaskName", assignedTaskName);
            }
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id,
                                          HttpServletRequest request) {
        ObjectData objectData = new ObjectData();
        MailBean mailBean = mailService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此郵件"));
        String chartName = chartService.getById(mailBean.getChartId())
                .map(ChartBean::getName)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        objectData.add("id", mailBean.getId());
        objectData.add("chartName", chartName);
        objectData.add("name", mailBean.getName());
        objectData.add("status", ProcessStatus.getProcessStatusName(mailBean.getStatus()));
        objectData.add("publisher", mailBean.getPublisher());
        objectData.add("receiver", mailBean.getReceiver());
        objectData.add("emailSendTime", mailBean.getEmailSendTime());
        if (mailBean.getAssignedTaskId() != null) {
            String assignedTaskName = assignedTaskService.getById(mailBean.getAssignedTaskId())
                    .map(AssignedTaskBean::getName)
                    .orElseThrow(() -> new NotFoundException("查無此交辦"));
            objectData.add("assignedTaskName", assignedTaskName);
        }
        ArrayData arrayData = objectData.addArray("messageList");
        for (MailMessageBean messageBean : messageService.searchByMailId(id)) {
            ObjectData messageData = arrayData.addObject();
            messageData.add("id", messageBean.getId());
            messageData.add("mailId", messageBean.getMailId());
            messageData.add("messageId", messageBean.getMessageId());
            messageData.add("content", messageBean.getContent());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }

    @PatchMapping("/message/{id}")
    public ResponseEntity<String> patchMessage(@PathVariable("id") Integer messageId,
                                               @RequestBody MailMessageBean messageBean,
                                               HttpServletRequest request) {
        messageService.update(messageId, messageBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delMail(@PathVariable("id") Integer id,
                                          HttpServletRequest request) {
        MailBean mailBean = mailService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此郵件"));
        if (!ProcessStatus.isSucceeded(mailBean.getStatus())) {
            return ResponseEntityBuilder.error()
                    .message("郵件為待處理狀態不能刪除")
                    .build();
        }
        mailBean.setAvailable(false);
        mailService.update(id, mailBean);
        return ResponseEntityBuilder.success()
                .message("刪除成功")
                .build();
    }
}
