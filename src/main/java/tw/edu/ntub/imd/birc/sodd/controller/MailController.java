package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.MailMessageService;
import tw.edu.ntub.imd.birc.sodd.service.MailService;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

@AllArgsConstructor
@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;
    private final MailMessageService messageService;

    @PostMapping("")
    public ResponseEntity<String> sendAssignMail(@RequestBody MailBean mailBean, BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        mailService.save(mailBean);
        return ResponseEntityBuilder.success()
                .message("交辦發送成功")
                .build();
    }

    @PostMapping("/message")
    public ResponseEntity<String> addMessage(@RequestBody MailMessageBean mailMessageBean,
                                             BindingResult bindingResult) {
        BindingResultUtils.validate(bindingResult);
        messageService.save(mailMessageBean);
        return ResponseEntityBuilder.success()
                .message("新增成功")
                .build();
    }

    @GetMapping("")
    public ResponseEntity<String> searchByStatus(@RequestParam("status") String status) {
        String userId = SecurityUtils.getLoginUserAccount();
        ArrayData arrayData = new ArrayData();
        for (MailBean mailBean : mailService.searchByStatus(userId, status)) {
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", mailBean.getId());
            objectData.add("assignedTaskId", mailBean.getAssignedTaskId());
            objectData.add("chartId", mailBean.getChartId());
            objectData.add("name", mailBean.getName());
            objectData.add("status", ProcessStatus.getProcessStatusName(mailBean.getStatus()));
            objectData.add("content", mailBean.getContent());
            objectData.add("publisher", mailBean.getPublisher());
            objectData.add("receiver", mailBean.getReceiver());
            objectData.add("emailSendTime", mailBean.getEmailSendTime());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(arrayData)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) {
        ObjectData objectData = new ObjectData();
        MailBean mailBean = mailService.getById(id)
                .orElseThrow(() -> new NotFoundException("查無此郵件"));
        objectData.add("id", mailBean.getId());
        objectData.add("assignedTaskId", mailBean.getAssignedTaskId());
        objectData.add("chartId", mailBean.getChartId());
        objectData.add("name", mailBean.getName());
        objectData.add("status", ProcessStatus.getProcessStatusName(mailBean.getStatus()));
        objectData.add("content", mailBean.getContent());
        objectData.add("publisher", mailBean.getPublisher());
        objectData.add("receiver", mailBean.getReceiver());
        objectData.add("emailSendTime", mailBean.getEmailSendTime());
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

    @PatchMapping("/message")
    public ResponseEntity<String> patchMessage(@PathVariable("id") Integer messageId,
                                               @RequestBody MailMessageBean messageBean) {
        messageService.update(messageId, messageBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delMail(@PathVariable("id") Integer id) {
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
