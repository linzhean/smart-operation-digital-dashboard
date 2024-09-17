package tw.edu.ntub.imd.birc.sodd.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.exception.UnknownException;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class EmailUtils {
    @Value("${sodd.mail.account}")
    private String FROM;
    private final JavaMailSender mailSender;

    public EmailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String receiver,
                         String userMail,
                         String subject,
                         String path,
                         String content) {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mimeMessageHelper.setFrom(FROM);
            mimeMessageHelper.setTo(userMail);
            mimeMessageHelper.setCc(userMail);
            mimeMessageHelper.setSubject(subject);
            String htmlContent = getHtmlContent(receiver, content, Files.readString(Paths.get(path)));
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

    private String getHtmlContent(String receiver, String content, String htmlContent) {
        if (StringUtils.isNotBlank(receiver)) {
            htmlContent = htmlContent.replace("{{receiver}}", receiver);
        }
        if (StringUtils.isNotBlank(content)) {
            htmlContent = htmlContent.replace("{{content}}", content);
        }
        return htmlContent;
    }
}
