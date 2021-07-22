package me.ixk.hoshi.mail.service;

import cn.hutool.core.util.RandomUtil;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.client.MailRemoteService;
import me.ixk.hoshi.mail.entity.RateLimit;
import me.ixk.hoshi.mail.entity.VerifyCode;
import me.ixk.hoshi.mail.exception.RateLimitException;
import me.ixk.hoshi.mail.repository.RateLimitRepository;
import me.ixk.hoshi.mail.repository.VerifyCodeRepository;
import me.ixk.hoshi.mail.view.CodeMail;
import me.ixk.hoshi.mail.view.CodeMail.CodeMailBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author Otstar Lin
 * @date 2021/7/19 21:36
 */
@Service
@RequiredArgsConstructor
public class VerifyCodeService {

    private static final Long RATE_LIMIT_TIMEOUT = 60L;
    private static final Integer RATE_LIMIT_COUNT = 10;
    private final MailRemoteService mailRemoteService;
    private final VerifyCodeRepository verifyCodeRepository;
    private final RateLimitRepository rateLimitRepository;

    public void generate(final String email, final String subject, final long timeout, final String ip) {
        this.generate(email, subject, timeout, ip, e -> RandomUtil.randomString(10).toUpperCase());
    }

    public void generate(
        final String email,
        final String subject,
        final long timeout,
        final String ip,
        final Function<String, String> generator
    ) {
        final Optional<RateLimit> rateLimitOptional = rateLimitRepository.findById(ip);
        final RateLimit rateLimit = rateLimitOptional.orElseGet(
            () -> RateLimit.builder().ip(ip).count(0).timeout(RATE_LIMIT_TIMEOUT).build()
        );
        rateLimit.setCount(rateLimit.getCount() + 1);
        if (rateLimit.getCount() > RATE_LIMIT_COUNT) {
            throw new RateLimitException("验证码获取过于频繁");
        }
        final String hash = this.createHash(subject);
        final Optional<VerifyCode> verifyCode = verifyCodeRepository.findByHashAndEmail(hash, email);
        final CodeMailBuilder mailBuilder = CodeMail.builder().to(email).subject(subject);
        if (verifyCode.isPresent()) {
            mailBuilder.code(verifyCode.get().getCode());
        } else {
            final String code = generator.apply(email);
            verifyCodeRepository.save(VerifyCode.builder().hash(hash).code(code).email(email).timeout(timeout).build());
            mailBuilder.code(code);
        }
        rateLimitRepository.save(rateLimit);
        mailRemoteService.sendCode(mailBuilder.build());
    }

    public boolean verify(final String subject, final String code) {
        final Optional<VerifyCode> verifyCode = this.find(subject, code);
        if (verifyCode.isEmpty()) {
            return false;
        }
        verifyCodeRepository.deleteById(verifyCode.get().getCode());
        return true;
    }

    public Optional<VerifyCode> find(final String subject, final String code) {
        return verifyCodeRepository.findByHashAndCode(createHash(subject), code);
    }

    private String createHash(final String subject) {
        return DigestUtils.md5DigestAsHex(subject.getBytes(StandardCharsets.UTF_8));
    }
}
