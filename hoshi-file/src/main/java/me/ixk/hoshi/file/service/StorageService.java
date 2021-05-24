package me.ixk.hoshi.file.service;

import cn.hutool.core.util.ArrayUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:09
 */
public interface StorageService extends AutoCloseable {
    /**
     * 初始化
     */
    void init();

    /**
     * 保存
     *
     * @param file  文件
     * @param paths 前缀
     * @return 文件名
     */
    StoreInfo store(MultipartFile file, String... paths);

    /**
     * 是否存在
     *
     * @param filename 文件名
     * @param paths    路径
     * @return 是否存在
     */
    boolean exist(String filename, String... paths);

    /**
     * 删除文件
     *
     * @param filename 文件名
     * @param paths    路径
     */
    void delete(String filename, String... paths);

    /**
     * 读取文件
     *
     * @param filename 文件名
     * @param paths    路径
     * @return 文件资源
     */
    Resource load(String filename, String... paths);

    /**
     * 读取文件列表
     *
     * @param paths 路径
     * @return 文件列表
     */
    Stream<Resource> loadAll(String... paths);

    /**
     * 删除所有文件
     *
     * @param paths 路径
     */
    void deleteAll(String... paths);

    /**
     * 关闭文件
     *
     * @throws Exception 异常
     */
    @Override
    void close() throws Exception;

    /**
     * 拼接路径的工具方法
     *
     * @param paths 路径
     * @return Path
     */
    default Path join(final String... paths) {
        if (paths == null || paths.length == 0) {
            throw new IllegalArgumentException("输入路径不能为空");
        }
        Path resolve = Paths.get(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            resolve = resolve.resolve(paths[i]);
        }
        return resolve;
    }

    /**
     * 拼接路径的工具方法
     *
     * @param paths    路径
     * @param filename 文件名
     * @return Path
     */
    default Path join(final String[] paths, final String filename) {
        return this.join(ArrayUtil.append(paths, filename));
    }

    @Data
    @Builder
    class StoreInfo {

        private final String mediaType;
        private final String extName;
        private final String fileName;
        private final long size;
        private final String path;
    }
}
