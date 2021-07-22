package me.ixk.hoshi.mail.repository;

import java.util.Optional;
import me.ixk.hoshi.mail.entity.VerifyCode;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Otstar Lin
 * @date 2021/7/20 21:31
 */
public interface VerifyCodeRepository extends CrudRepository<VerifyCode, String> {
    Optional<VerifyCode> findByHashAndCode(String hash, String code);

    Optional<VerifyCode> findByHashAndEmail(String hash, String email);
}
