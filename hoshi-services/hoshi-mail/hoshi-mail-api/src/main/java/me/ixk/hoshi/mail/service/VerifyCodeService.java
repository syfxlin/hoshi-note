package me.ixk.hoshi.mail.service;

import cn.hutool.core.util.RandomUtil;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.client.MailRemoteService;
import me.ixk.hoshi.mail.view.CodeMail;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author Otstar Lin
 * @date 2021/7/19 21:36
 */
@Service
@RequiredArgsConstructor
public class VerifyCodeService {

    private final MailRemoteService mailRemoteService;

    public void generate(final String to, final String subject, final HttpSession session) {
        generate(to, subject, 8, session);
    }

    public void generate(final String to, final String subject, final int length, final HttpSession session) {
        final String code = RandomUtil.randomString(length).toUpperCase();
        final CodeMail mail = CodeMail.builder().to(to).subject(subject).code(code).build();
        session.setAttribute(
            "verify-code:" + DigestUtils.md5DigestAsHex(subject.getBytes(StandardCharsets.UTF_8)),
            code
        );
        mailRemoteService.sendCode(mail);
    }

    public boolean verify(final String subject, final String code, final HttpSession session) {
        final String id = "verify-code:" + DigestUtils.md5DigestAsHex(subject.getBytes(StandardCharsets.UTF_8));
        final Object load = session.getAttribute(id);
        session.removeAttribute(id);
        return code.equals(load);
    }
}
