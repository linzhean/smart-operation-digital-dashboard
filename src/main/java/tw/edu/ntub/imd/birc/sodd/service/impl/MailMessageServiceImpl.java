package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.MailMessageDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.MailMessage;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.MailMessageService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailMessageTransformer;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MailMessageServiceImpl extends BaseServiceImpl<MailMessageBean, MailMessage, Integer> implements MailMessageService {
    private final MailMessageDAO mailMessageDAO;
    private final MailMessageTransformer transformer;

    public MailMessageServiceImpl(MailMessageDAO mailMessageDAO, MailMessageTransformer transformer) {
        super(mailMessageDAO, transformer);
        this.mailMessageDAO = mailMessageDAO;
        this.transformer = transformer;
    }


    @Override
    public MailMessageBean save(MailMessageBean mailMessageBean) {
        MailMessage mailMessage = transformer.transferToEntity(mailMessageBean);
        return transformer.transferToBean(mailMessageDAO.save(mailMessage));
    }


    @Override
    public List<MailMessageBean> searchByMailId(Integer mailId) {
        List<MailMessage> resultList = new ArrayList<>();
        List<MailMessage> mailMessageList = mailMessageDAO.findByMailId(mailId);
        if (mailMessageList.isEmpty()) {
            return CollectionUtils.map(mailMessageList, transformer::transferToBean);
        }
        MailMessage firstMessage = mailMessageList.stream()
                .filter(message -> message.getMessageId() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("查無此郵件訊息"));
        return CollectionUtils.map(searchChildMessage(resultList, firstMessage), transformer::transferToBean);
    }

    private List<MailMessage> searchChildMessage(List<MailMessage> messageList, MailMessage message) {
        messageList.add(message);
        Optional<MailMessage> messageOptional = mailMessageDAO
                .findByMessageIdAndAvailableIsTrue(message.getId())
                .stream()
                .filter(messageBean -> Objects.equals(messageBean.getMessageId(), message.getId()))
                .findAny();
        messageOptional.ifPresent(mailMessage -> searchChildMessage(messageList, mailMessage));
        return messageList;
    }

    @Override
    public List<MailMessageBean> searchByMessageId(Integer messageId) {
        return CollectionUtils.map(
                mailMessageDAO.findByMessageIdAndAvailableIsTrue(messageId), transformer::transferToBean);
    }
}
