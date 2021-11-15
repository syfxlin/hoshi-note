/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mysql.repository;

import java.util.Optional;
import java.util.function.Function;
import me.ixk.hoshi.mysql.util.Jpa;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.util.Assert;

/**
 * 更新功能的 {@link Repository}
 * <p>
 * 进行一级判断，将新的实体赋值到旧实体上，为 <code>null</code> 则代表未设置，保留原值
 *
 * @author Otstar Lin
 * @date 2021/6/5 14:31
 */
public interface UpdatingRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    /**
     * 更新
     *
     * @param entity 更新的实体，为 null 字段表示不更新
     * @param getId  获取 ID
     * @return 更新后的实体
     */
    default T update(final T entity, final Function<T, ID> getId) {
        Assert.notNull(entity, "传入实体必须不为空");
        final ID id = getId.apply(entity);
        Assert.notNull(id, "更新时 ID 必须设置");
        final Optional<T> target = this.findById(id);
        if (target.isEmpty()) {
            return this.save(entity);
        }
        return this.save(Jpa.merge(entity, target.get()));
    }
}
