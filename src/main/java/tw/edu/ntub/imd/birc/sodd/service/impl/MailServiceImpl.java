package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.MailDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.MailSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.exception.NoPermissionException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.service.MailMessageService;
import tw.edu.ntub.imd.birc.sodd.service.MailService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailTransformer;
import tw.edu.ntub.imd.birc.sodd.util.email.EmailUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MailServiceImpl extends BaseServiceImpl<MailBean, Mail, Integer> implements MailService {
    private final MailDAO mailDAO;
    private final MailTransformer transformer;
    private final AssignedTaskSponsorService sponsorService;
    private final MailMessageService messageService;
    private final UserAccountDAO userAccountDAO;
    private final EmailUtils emailUtils;
    private final MailSpecification specification;
    @Value("${sodd.mail.template.AssignTaskMail}")
    private String mailPath;
    public MailServiceImpl(MailDAO mailDAO,
                           MailTransformer transformer,
                           AssignedTaskSponsorService sponsorService,
                           MailMessageService messageService,
                           UserAccountDAO userAccountDAO,
                           EmailUtils emailUtils,
                           MailSpecification specification) {
        super(mailDAO, transformer);
        this.mailDAO = mailDAO;
        this.transformer = transformer;
        this.sponsorService = sponsorService;
        this.messageService = messageService;
        this.userAccountDAO = userAccountDAO;
        this.emailUtils = emailUtils;
        this.specification = specification;
    }

    @Override
    public MailBean save(MailBean mailBean) {
        String userId = SecurityUtils.getLoginUserAccount();
        Mail mail = transformer.transferToEntity(mailBean);
        boolean isSponsor = sponsorService.findByUserId(userId)
                .stream()
                .anyMatch(sponsorBean -> sponsorBean.getChartId().equals(mailBean.getChartId()));
        MailMessageBean firstMes = mailBean.getFirstMessage();
        if (isSponsor) {
            String userMail = userAccountDAO.findById(mailBean.getReceiver())
                    .map(UserAccount::getGmail)
                    .orElseThrow(() -> new NotFoundException("查無此使用者"));
            emailUtils.sendMail(mailBean.getReceiver(), userMail, "數位儀表板交辦事項",
                    mailPath, firstMes.getContent());
            mail = mailDAO.save(mail);
            firstMes.setMailId(mail.getId());
            messageService.save(firstMes);
            return transformer.transferToBean(mail);
        }
        throw new NoPermissionException();
    }


    @Override
    public List<MailBean> searchByStatus(String userId, String status) {
        return CollectionUtils.map(
                mailDAO.findAll(specification.checkBlank(userId, status)), transformer::transferToBean);
    }

    @Override
    public void sendSystemAssignedTask(AssignedTasks assignedTasks,
                                       Chart chart,
                                       BigDecimal ratio,
                                       UserAccount userAccount,
                                       String status,
                                       Double standard) {
        MailBean mailBean = new MailBean();
        mailBean.setAssignedTaskId(assignedTasks.getChartId());
        mailBean.setChartId(chart.getId());
        mailBean.setName("系統警告: " + chart.getName() + "超過標準上下限");
        mailBean.setPublisher(assignedTasks.getDefaultAuditor());
        mailBean.setReceiver(assignedTasks.getDefaultProcessor());
        mailBean.setCreateId("system");
        mailBean.setModifyId("system");
        MailMessageBean messageBean = new MailMessageBean();
        messageBean.setContent(String.format(
                "來自系統的警告！目前的%s為 %s，%s於達成率門檻: %s。請立即檢查並採取措施！",
                chart.getName(), ratio, status, standard));
        mailBean.setFirstMessage(messageBean);
        Mail mail = mailDAO.save(transformer.transferToEntity(mailBean));
        messageBean.setMailId(mail.getId());
        messageBean.setCreateId("system");
        messageBean.setModifyId("system");
        messageService.save(messageBean);
        emailUtils.sendMail(userAccount.getUserId(), userAccount.getGmail(),
                chart.getName() + "資料超過上下限通知",
                mailPath, messageBean.getContent());
    }
}
