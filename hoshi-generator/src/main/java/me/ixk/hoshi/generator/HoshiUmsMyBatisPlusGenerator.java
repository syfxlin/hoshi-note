/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.generator;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Otstar Lin
 * @date 2021/5/5 下午 12:19
 */
public class HoshiUmsMyBatisPlusGenerator {

    @SuppressWarnings("unchecked")
    public static void main(final String[] args) {
        // 根目录
        final String rootPath = System.getProperty("user.dir");
        // 代码生成器
        final AutoGenerator mpg = new AutoGenerator();
        // 配置文件
        final Yaml yaml = new Yaml();
        final Map<String, Object> config = yaml.load(
            FileUtil.getUtf8Reader(rootPath + "/static/config/application.yml")
        );
        // 全局配置
        final GlobalConfig gc = new GlobalConfig();
        // 设置输出目录
        gc.setOutputDir(rootPath + "/hoshi-ums/src/main/java");
        // 设置注释中的作者
        gc.setAuthor("syfxlin");
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(true);
        // Swagger
        gc.setSwagger2(true);
        gc.setServiceName("%sService");

        // 数据源配置
        final DataSourceConfig dsc = new DataSourceConfig();
        final Map<String, String> datasource =
            ((Map<String, Map<String, String>>) config.get("spring")).get("datasource");
        dsc.setUrl(datasource.get("url"));
        dsc.setDriverName(datasource.get("driver-class-name"));
        dsc.setUsername(datasource.get("username"));
        dsc.setPassword(datasource.get("password"));

        // 包配置
        final PackageConfig pc = new PackageConfig();
        // 设置父级包名
        pc.setParent("me.ixk.hoshi.ums");

        // 策略配置
        final StrategyConfig strategy = new StrategyConfig();
        // 下划线转驼峰，表名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 下划线转驼峰，列名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表前缀
        strategy.setTablePrefix(pc.getModuleName() + "_");
        // 控制器配置
        strategy.setRestControllerStyle(true);
        // Mapping 样式
        strategy.setControllerMappingHyphenStyle(true);
        // 开启 Lombok 模式
        strategy.setEntityLombokModel(true);
        // 链式
        strategy.setChainModel(true);
        // 序列化 ID
        strategy.setEntitySerialVersionUID(true);
        // 字段常量
        strategy.setEntityColumnConstant(true);
        // 去掉 is_ 前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        mpg.setDataSource(dsc);
        mpg.setPackageInfo(pc);
        mpg.setGlobalConfig(gc);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
