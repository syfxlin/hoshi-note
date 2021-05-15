package me.ixk.hoshi.generator;

import me.ixk.hoshi.generator.util.MyBatisGenerator;

/**
 * @author Otstar Lin
 * @date 2021/5/15 上午 10:03
 */
public class HoshiSecurityMyBatisPlusGenerator {

    public static void main(final String[] args) {
        MyBatisGenerator.generator("hoshi-security", "me.ixk.hoshi.security");
    }
}
