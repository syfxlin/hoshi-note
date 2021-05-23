package me.ixk.hoshi.file.service;

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
     * @return 文件列表
     */
    Stream<Resource> loadAll();

    /**
     * 删除所有文件
     */
    void deleteAll();

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
     * @param filename 文件名
     * @param paths    路径
     * @return Path
     */
    default Path join(final String filename, final String... paths) {
        if (filename == null) {
            throw new IllegalArgumentException("输入文件名不能为 null");
        }
        if (paths.length == 0) {
            return Paths.get(filename);
        }
        Path resolve = Paths.get(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            resolve = resolve.resolve(paths[i]);
        }
        return resolve.resolve(filename);
    }

    @Data
    @Builder
    class StoreInfo {

        private final String extname;
        private final String filename;
        private final long size;
        private final String path;
    }
}
