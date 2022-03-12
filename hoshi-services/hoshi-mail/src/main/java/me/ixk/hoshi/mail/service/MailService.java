package me.ixk.hoshi.mail.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.exception.SendException;
import me.ixk.hoshi.mail.view.Attachment;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Configuration configuration;

    @Value("${spring.mail.from}")
    private String from;

    public void send(final Mail mail) {
        mailSender.send(this.toMessage(mail));
    }

    public void sendCode(final CodeMail mail) {
        final StringWriter writer = new StringWriter();
        final Map<String, Object> model = this.createModel();
        model.put("subject", mail.getSubject());
        model.put("code", mail.getCode());
        try {
            configuration.getTemplate("code.ftlh").process(model, writer);
        } catch (final TemplateException | IOException e) {
            throw new SendException("模板解析失败", e);
        }
        this.send(mail.toMail(writer.toString()));
    }

    private Map<String, Object> createModel() {
        final Map<String, Object> map = new HashMap<>(16);
        map.put("helpUrl", "https://github.com/syfxlin/hoshi-note");
        return map;
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
