package tw.edu.ntub.imd.birc.sodd.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;
import tw.edu.ntub.imd.birc.sodd.exception.BircException;
import tw.edu.ntub.imd.birc.sodd.exception.DataAlreadyExistException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.*;
import tw.edu.ntub.imd.birc.sodd.util.http.BindingResultUtils;
import tw.edu.ntub.imd.birc.sodd.util.http.ResponseEntityBuilder;
import tw.edu.ntub.imd.birc.sodd.util.json.array.ArrayData;
import tw.edu.ntub.imd.birc.sodd.util.json.object.ObjectData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/mail")
@PreAuthorize(SecurityUtils.NOT_NO_PERMISSION_AUTHORITY)
public class MailController {
    private final MailService mailService;
    private final MailMessageService messageService;
    private final ChartService chartService;
    private final AssignedTaskService assignedTaskService;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

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
    @SendTo("/topic/newMessage")
    public ResponseEntity<String> addMessage(@RequestParam("mailId") Integer mailId,
                                             @Valid @RequestBody MailMessageBean mailMessageBean,
                                             BindingResult bindingResult,
                                             HttpServletRequest request) {
        BindingResultUtils.validate(bindingResult);
        messageService.getById(mailMessageBean.getMessageId())
                .orElseThrow(() -> new NotFoundException("查無此訊息"));
        messageService.searchByMessageId(mailMessageBean.getMessageId())
                .stream()
                .filter(messageBean -> Objects.equals(messageBean.getMessageId(), mailMessageBean.getMessageId()))
                .findAny()
                .ifPresent(messageBean -> {
                    throw new DataAlreadyExistException("此則訊息ID已被ID:" +
                            messageBean.getMessageId() + "使用過了，請傳入新的訊息ID");
                });
        mailMessageBean.setMailId(mailId);
        messageService.save(mailMessageBean);
        messagingTemplate.convertAndSend("/topic/newMessage", mailMessageBean);
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
            ChartBean chartBean = chartService.getById(mailBean.getChartId())
                    .orElseThrow(() -> new NotFoundException("查無此圖表"));
            ObjectData objectData = arrayData.addObject();
            objectData.add("id", mailBean.getId());
            objectData.add("chartName", chartBean.getName());
            objectData.add("showcaseImage", chartBean.getShowcaseImage());
            objectData.add("name", mailBean.getName());
            objectData.add("status", ProcessStatus.getProcessStatusName(mailBean.getStatus()));
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
        ArrayData arrayData = objectData.addArray("messageList");
        for (MailMessageBean messageBean : messageService.searchByMailId(id)) {
            ObjectData messageData = arrayData.addObject();
            messageData.add("id", messageBean.getId());
            messageData.add("mailId", messageBean.getMailId());
            messageData.add("messageId", messageBean.getMessageId());
            messageData.add("content", messageBean.getContent());
            messageData.add("createId", messageBean.getCreateId());
            messageData.add("createDate", messageBean.getCreateDate());
        }
        return ResponseEntityBuilder.success()
                .message("查詢成功")
                .data(objectData)
                .build();
    }

    @PatchMapping("/message/{messageId}")
    public ResponseEntity<String> patchMessage(@PathVariable("messageId") Integer messageId,
                                               @RequestBody MailMessageBean messageBean,
                                               HttpServletRequest request) {
        messageService.update(messageId, messageBean);
        return ResponseEntityBuilder.success()
                .message("更新成功")
                .build();
    }

    @PatchMapping("/message/status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") Integer mailId,
                                               HttpServletRequest request) {
        MailBean mailBean = mailService.getById(mailId)
                .orElseThrow(() -> new NotFoundException("查無此郵件"));
        if (!ProcessStatus.isPending(mailBean.getStatus())) {
            return ResponseEntityBuilder.error()
                    .message("此郵件已為已完成狀態")
                    .build();
        }
        if (mailBean.getPublisher().equals(SecurityUtils.getLoginUserAccount())) {
            return ResponseEntityBuilder.error()
                    .message("非發送交辦事項的使用者無法修改為已完成狀態")
                    .build();
        }
        mailBean.setStatus(ProcessStatus.SUCCEEDED);
        mailService.update(mailId, mailBean);
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
