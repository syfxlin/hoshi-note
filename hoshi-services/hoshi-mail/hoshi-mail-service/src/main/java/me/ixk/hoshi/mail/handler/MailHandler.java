package me.ixk.hoshi.mail.handler;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.service.MailService;
import me.ixk.hoshi.mail.view.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/7/17 17:57
 */
@Component
@RequiredArgsConstructor
public class MailHandler {

    private final MailService mailService;

    @RabbitListener(queues = "email")
    public void sendMail(final Mail mail) {
        mailService.send(mail);
    }
}
