/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mail.handler;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.service.MailService;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/7/17 17:57
 */
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "email")
public class MailHandler {

    private final MailService mailService;

    @RabbitHandler
    public void send(final Mail mail) {
        mailService.send(mail);
    }

    @RabbitHandler
    public void sendCode(final CodeMail mail) {
        mailService.sendCode(mail);
    }
}
