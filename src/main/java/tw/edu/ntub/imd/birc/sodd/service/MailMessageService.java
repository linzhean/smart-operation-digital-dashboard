package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;

import java.util.List;

public interface MailMessageService extends BaseService<MailMessageBean, Integer> {
    List<MailMessageBean> searchByMailId(Integer mailId);

    List<MailMessageBean> searchByMessageId(Integer messageId);
}
