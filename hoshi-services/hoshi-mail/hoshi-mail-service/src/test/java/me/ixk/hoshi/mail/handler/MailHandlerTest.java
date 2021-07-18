package me.ixk.hoshi.mail.handler;

import cn.hutool.core.util.RandomUtil;
import me.ixk.hoshi.mail.client.MailRemoteService;
import me.ixk.hoshi.mail.view.Attachment;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.Mail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/**
 * @author Otstar Lin
 * @date 2021/7/17 18:06
 */
@SpringBootTest
class MailHandlerTest {

    @Autowired
    private MailRemoteService mailRemoteService;

    @Value("classpath:application.yml")
    Resource ymlFile;

    @Test
    void send() {
        final Attachment attachment = Attachment
            .builder()
            .name("application.yml")
            .source(ymlFile)
            .inline(false)
            .contentType("text/yaml")
            .build();
        final Mail mail = Mail
            .builder()
            .to("i@ixk.me")
            .subject("Test mail queue")
            .text("Test mail queue")
            .attachment(attachment)
            .build();
        mailRemoteService.send(mail);
    }

    @Test
    void sendCode() {
        final CodeMail mail = CodeMail
            .builder()
            .to("i@ixk.me")
            .subject("验证您的邮箱账户")
            .code(RandomUtil.randomString(6))
            .expire(30)
            .build();
        mailRemoteService.sendCode(mail);
    }
}
