/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.db.generator;

import cn.hutool.core.lang.ObjectId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * ObjectId 主键生成器
 *
 * @author Otstar Lin
 * @date 2021/11/17 21:50
 */
public class ObjectIdGenerator implements IdentifierGenerator {

    public static final String STRATEGY_NAME = "me.ixk.hoshi.mysql.generator.ObjectIdGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return ObjectId.next();
    }
}
