package me.ixk.hoshi.mail.repository;

import me.ixk.hoshi.mail.entity.VerifyCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author Otstar Lin
 * @date 2021/7/20 21:31
 */
public interface VerifyCodeRepository extends CrudRepository<VerifyCode, String>, QueryByExampleExecutor<VerifyCode> {}
