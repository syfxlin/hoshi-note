/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mail.service;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.Mail;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Otstar Lin
 * @date 2021/7/17 21:23
 */
@Service
@RequiredArgsConstructor
public class MailRemoteService {

    private final AmqpTemplate amqpTemplate;

    public void send(final Mail mail) {
        amqpTemplate.convertAndSend("email", mail);
    }

    public void sendCode(final CodeMail mail) {
        amqpTemplate.convertAndSend("email", mail);
    }
}
