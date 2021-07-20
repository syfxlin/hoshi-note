package me.ixk.hoshi.mail.repository;

import me.ixk.hoshi.mail.entity.RateLimit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author Otstar Lin
 * @date 2021/7/20 21:31
 */
public interface RateLimitRepository extends CrudRepository<RateLimit, String>, QueryByExampleExecutor<RateLimit> {}
