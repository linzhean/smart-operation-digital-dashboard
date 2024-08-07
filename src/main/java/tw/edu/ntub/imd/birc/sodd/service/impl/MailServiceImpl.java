package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.exception.UnknownException;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.bean.MailMessageBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.MailDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.MailSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskSponsorService;
import tw.edu.ntub.imd.birc.sodd.service.MailMessageService;
import tw.edu.ntub.imd.birc.sodd.service.MailService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.MailTransformer;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MailServiceImpl extends BaseServiceImpl<MailBean, Mail, Integer> implements MailService {
    @Value("${sodd.mail.file-path}")
    private String PATH;
    @Value("${sodd.mail.subject}")
    private String SUBJECT;
    @Value("${sodd.mail.account}")
    private String FROM;

    private final MailDAO mailDAO;
    private final MailTransformer transformer;
    private final AssignedTaskSponsorService sponsorService;
    private final MailMessageService messageService;
    private final AssignedTaskService assignedTaskService;
    private final UserAccountDAO userAccountDAO;
    private final JavaMailSender mailSender;
    private final MailSpecification specification;

    public MailServiceImpl(MailDAO mailDAO,
                           MailTransformer transformer,
                           AssignedTaskSponsorService sponsorService,
                           MailMessageService messageService,
                           AssignedTaskService assignedTaskService,
                           UserAccountDAO userAccountDAO,
                           JavaMailSender mailSender,
                           MailSpecification specification) {
        super(mailDAO, transformer);
        this.mailDAO = mailDAO;
        this.transformer = transformer;
        this.sponsorService = sponsorService;
        this.messageService = messageService;
        this.assignedTaskService = assignedTaskService;
        this.userAccountDAO = userAccountDAO;
        this.mailSender = mailSender;
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
            sendMail(mailBean.getReceiver(), firstMes.getContent());
            mail = mailDAO.save(mail);
            firstMes.setMailId(mail.getId());
            messageService.save(firstMes);
            return transformer.transferToBean(mail);
        }
        throw new AccessDeniedException("您並無針對此圖表發送交辦事項的權限");
    }

    private void sendMail(String receiver, String content) {
        String userMail = userAccountDAO.findById(receiver)
                .map(UserAccount::getGmail)
                .orElseThrow(() -> new NotFoundException("查無此使用者"));
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mimeMessageHelper.setFrom(FROM);
            mimeMessageHelper.setTo(userMail);
            mimeMessageHelper.setCc(userMail);
            mimeMessageHelper.setSubject(SUBJECT);
            String htmlContent = Files.readString(Paths.get(PATH));
            htmlContent = htmlContent.replace("{{receiver}}", receiver);
            htmlContent = htmlContent.replace("{{content}}", content);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessageHelper.getMimeMessage().setContent(multipart);
            try {
                mailSender.send(mimeMessageHelper.getMimeMessage());
            } catch (Exception e) {
                System.err.println(e.toString());
                throw new NotFoundException("Email格式錯誤，請重新確認");
            }
        } catch (MessagingException e) {
            throw new UnknownException(e);
        } catch (IOException e) {
            throw new RuntimeException("檔案讀取失敗");
        }
    }

    @Override
    public List<MailBean> searchByStatus(String userId, String status) {
        return CollectionUtils.map(
                mailDAO.findAll(specification.checkBlank(userId, status)), transformer::transferToBean);
    }
}
