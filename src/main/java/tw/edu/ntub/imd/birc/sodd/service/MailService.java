package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.MailBean;

import java.util.List;

public interface MailService extends BaseService<MailBean, Integer> {
    List<MailBean> searchByStatus(String userId, String status);
}
