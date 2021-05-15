/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.generator;

import me.ixk.hoshi.generator.util.MyBatisGenerator;

/**
 * @author Otstar Lin
 * @date 2021/5/5 下午 12:19
 */
public class HoshiUmsMyBatisPlusGenerator {

    public static void main(final String[] args) {
        MyBatisGenerator.generator("hoshi-ums", "me.ixk.hoshi.ums");
    }
}
