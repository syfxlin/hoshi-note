package me.ixk.hoshi.file.service;

import cn.hutool.core.io.FileUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import me.ixk.hoshi.file.exception.StorageException;
import me.ixk.hoshi.file.util.Mime;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:38
 */
public class FileStorageService implements StorageService {

    private final Path location;

    public FileStorageService(final Path location) {
        this.location = location;
    }

    @PostConstruct
    @Override
    public void init() {
        FileUtil.mkdir(this.location.toFile());
    }

    @Override
    public StoreInfo store(final MultipartFile file, final String... paths) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("不能存储空文件");
            }
            final BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
            final MediaType mediaType = Mime.media(bis, file.getOriginalFilename());
            final MimeType mimeType = Mime.mime(mediaType);
            final String ext = mimeType.getExtension();
            final String uuid = UUID.randomUUID().toString();
            final String filename = uuid + ext;
            final Path resolve = this.join(filename, paths);
            final Path path = this.location.resolve(resolve).normalize().toAbsolutePath();
            if (!path.startsWith(this.location.toAbsolutePath())) {
                throw new StorageException("不能将文件保存到文件系统外（非法操作）");
            }
            FileUtil.mkParentDirs(path.toFile());
            try (final InputStream stream = file.getInputStream()) {
                Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
            }
            return StoreInfo
                .builder()
                .path(resolve.toString())
                .extName(ext)
                .mediaType(mediaType.toString())
                .fileName(filename)
                .size(file.getSize())
                .build();
        } catch (final IOException e) {
            throw new StorageException("保存文件失败", e);
        }
    }

    @Override
    public boolean exist(final String filename, final String... paths) {
        return this.location.resolve(this.join(filename, paths)).toFile().exists();
    }

    @Override
    public void delete(final String filename, final String... paths) {
        FileUtil.del(this.location.resolve(this.join(filename, paths)));
    }

    @Override
    public Resource load(final String filename, final String... paths) {
        return new FileSystemResource(this.location.resolve(this.join(filename, paths)));
    }

    @Override
    public Stream<Resource> loadAll() {
        try {
            return Files
                .walk(this.location, 1)
                .filter(path -> !path.equals(this.location))
                .map(FileSystemResource::new);
        } catch (final IOException e) {
            throw new StorageException("无法读取文件列表", e);
        }
    }

    @Override
    public void deleteAll() {
        FileUtil.del(this.location);
    }

    @PreDestroy
    @Override
    public void close() throws Exception {}
}
