package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.MailMessageDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.MailMessage;
import tw.edu.ntub.imd.birc.sodd.service.MailMessageService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailMessageTransformer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        mailMessageDAO.findByMailId(mailMessageBean.getMailId())
                .stream().max(Comparator.comparing(MailMessage::getMessageId))
                .ifPresent(message -> mailMessage.setMessageId(message.getMessageId()));
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
                .get();
        resultList.add(firstMessage);
        mailMessageList.remove(firstMessage);
        if (mailMessageList.isEmpty()) {
            return CollectionUtils.map(mailMessageList, transformer::transferToBean);
        }
        mailMessageList.stream()
                .sorted(Comparator.comparing(MailMessage::getMessageId))
                .map(message -> resultList.add(message));
        return CollectionUtils.map(resultList, transformer::transferToBean);
    }


}
