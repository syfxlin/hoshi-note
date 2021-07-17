package me.ixk.hoshi.mail.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.view.Attachment;
import me.ixk.hoshi.mail.view.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * 邮箱服务
 *
 * @author Otstar Lin
 * @date 2021/7/17 16:34
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void send(final Mail mail) {
        mailSender.send(this.toMessage(mail));
    }

    private MimeMessagePreparator toMessage(final Mail mail) {
        return mimeMessage -> {
            final List<Attachment> attachments = mail.getAttachments();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, attachments.size() > 0);
            final String replyTo = mail.getReplyTo();
            helper.setReplyTo(replyTo == null ? from : replyTo);
            helper.setTo(mail.getTo().toArray(String[]::new));
            helper.setCc(mail.getCc().toArray(String[]::new));
            helper.setBcc(mail.getBcc().toArray(String[]::new));
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getText(), mail.isHtml());
            helper.setFrom(from);
            for (final Attachment attachment : attachments) {
                final String contentType = attachment.getContentType();
                final InputStreamSource source = attachment.getSource();
                final String name = attachment.getName();
                if (attachment.isInline()) {
                    helper.addInline(name, source, contentType);
                } else {
                    helper.addAttachment(name, source, contentType);
                }
            }
        };
    }
}
