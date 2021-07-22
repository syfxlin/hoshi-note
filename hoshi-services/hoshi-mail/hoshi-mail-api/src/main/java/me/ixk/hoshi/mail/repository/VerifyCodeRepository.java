package me.ixk.hoshi.mail.repository;

import java.util.Optional;
import me.ixk.hoshi.mail.entity.VerifyCode;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Otstar Lin
 * @date 2021/7/20 21:31
 */
public interface VerifyCodeRepository extends CrudRepository<VerifyCode, String> {
    /**
     * 通过 hash 和 code 查找验证码
     *
     * @param hash 区别用途
     * @param code 验证码
     * @return 验证码实体
     */
    Optional<VerifyCode> findByHashAndCode(String hash, String code);

    /**
     * 通过 hash 和 email 查找验证码
     *
     * @param hash  区别用途
     * @param email 邮箱
     * @return 验证码实体
     */
    Optional<VerifyCode> findByHashAndEmail(String hash, String email);
}
