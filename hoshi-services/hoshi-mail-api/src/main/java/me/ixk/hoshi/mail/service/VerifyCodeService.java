/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mail.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.mail.entity.RateLimit;
import me.ixk.hoshi.mail.entity.VerifyCode;
import me.ixk.hoshi.mail.entity.VerifyCode.VerifyCodeBuilder;
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
    private final MailRemoteService mailService;
    private final VerifyCodeRepository verifyCodeRepository;
    private final RateLimitRepository rateLimitRepository;

    public void generate(
        final String email,
        final String subject,
        final Long timeout,
        final String ip,
        final String code
    ) {
        final Optional<RateLimit> rateLimitOptional = rateLimitRepository.findById(ip);
        final RateLimit rateLimit = rateLimitOptional.orElseGet(() ->
            RateLimit.builder().ip(ip).count(0).timeout(RATE_LIMIT_TIMEOUT).build()
        );
        rateLimit.setCount(rateLimit.getCount() + 1);
        if (rateLimit.getCount() > RATE_LIMIT_COUNT) {
            throw new RateLimitException("验证码获取过于频繁");
        }
        final String hash = this.createHash(subject);
        final Optional<VerifyCode> verifyCodeOptional = verifyCodeRepository.findByHashAndEmail(hash, email);
        final CodeMailBuilder mailBuilder = CodeMail.builder().to(email).subject(subject);
        final VerifyCodeBuilder verifyCodeBuilder = VerifyCode.builder().hash(hash).code(code).email(email);
        // 如果验证码在有效期内被重新获取则删除旧的验证码，重新将新的验证码存入
        if (verifyCodeOptional.isPresent()) {
            final VerifyCode verifyCode = verifyCodeOptional.get();
            verifyCodeRepository.deleteById(verifyCode.getCode());
            verifyCodeBuilder.timeout(verifyCode.getTimeout());
        } else {
            verifyCodeBuilder.timeout(timeout);
        }
        verifyCodeRepository.save(verifyCodeBuilder.build());
        rateLimitRepository.save(rateLimit);
        mailBuilder.code(code);
        mailService.sendCode(mailBuilder.build());
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
        final Optional<VerifyCode> verifyCode = verifyCodeRepository.findByHashAndCode(createHash(subject), code);
        if (verifyCode.isEmpty()) {
            return verifyCode;
        }
        verifyCodeRepository.deleteById(verifyCode.get().getCode());
        return verifyCode;
    }

    private String createHash(final String subject) {
        return DigestUtils.md5DigestAsHex(subject.getBytes(StandardCharsets.UTF_8));
    }
}
