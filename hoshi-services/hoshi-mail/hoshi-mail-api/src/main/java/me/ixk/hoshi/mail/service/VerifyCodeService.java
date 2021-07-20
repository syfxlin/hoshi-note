package me.ixk.hoshi.mail.service;

import cn.hutool.core.util.RandomUtil;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.client.MailRemoteService;
import me.ixk.hoshi.mail.entity.VerifyCode;
import me.ixk.hoshi.mail.repository.RateLimitRepository;
import me.ixk.hoshi.mail.repository.VerifyCodeRepository;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.CodeMail.CodeMailBuilder;
import org.springframework.data.domain.Example;
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
    private final VerifyCodeRepository verifyCodeRepository;
    private final RateLimitRepository rateLimitRepository;

    public void generate(final String email, final String subject, final long timeout) {
        final String hash = this.createHash(subject);
        final Optional<VerifyCode> verifyCode = verifyCodeRepository.findOne(
            Example.of(VerifyCode.builder().hash(hash).email(email).build())
        );
        final CodeMailBuilder mailBuilder = CodeMail.builder().to(email).subject(subject);
        if (verifyCode.isPresent()) {
            mailBuilder.code(verifyCode.get().getCode());
        } else {
            final String code = RandomUtil.randomString(10).toUpperCase();
            verifyCodeRepository.save(VerifyCode.builder().hash(hash).code(code).email(email).timeout(timeout).build());
            mailBuilder.code(code);
        }
        mailRemoteService.sendCode(mailBuilder.build());
    }

    public boolean verify(final String subject, final String code) {
        return verifyCodeRepository.exists(Example.of(VerifyCode.builder().hash(subject).code(code).build()));
    }

    private String createHash(final String subject) {
        return DigestUtils.md5DigestAsHex(subject.getBytes(StandardCharsets.UTF_8));
    }
}
