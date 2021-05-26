package me.ixk.hoshi.file.controller;

import static me.ixk.hoshi.security.util.Security.USER_ATTR;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.file.exception.StorageException;
import me.ixk.hoshi.file.service.StorageService;
import me.ixk.hoshi.file.service.StorageService.StoreInfo;
import me.ixk.hoshi.file.util.Mime;
import me.ixk.hoshi.file.view.FileView;
import me.ixk.hoshi.file.view.UploadView;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Otstar Lin
 * @date 2021/5/23 16:12
 */
@RestController
@RequestMapping("/api/images")
@Api("图片控制器")
@RequiredArgsConstructor
public class ImageController {

    public static final String FILE_DIR = "user-image";
    private final StorageService storageService;

    @ApiOperation("列出图片")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<List<FileView>> list(final PageView vo, @ModelAttribute(USER_ATTR) final User user) {
        Stream<Resource> all = this.storageService.loadAll(FILE_DIR, user.getId());
        if (vo.getPage() != null) {
            all = all.skip(((long) vo.getPage() - 1L) * vo.getPageSize()).limit(vo.getPageSize());
        }
        return ApiResult.ok(
            all
                .map(
                    r -> {
                        try {
                            return FileView
                                .builder()
                                .fileName(r.getFilename())
                                .size(r.contentLength())
                                .url(String.format("/api/files/%s/%s", user.getId().toString(), r.getFilename()))
                                .build();
                        } catch (final IOException e) {
                            throw new StorageException("获取文件大小失败", e);
                        }
                    }
                )
                .collect(Collectors.toList())
        );
    }

    @ApiOperation("上传图片")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResult<UploadView> upload(
        @RequestParam("file") final MultipartFile file,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        final StoreInfo store = this.storageService.store(file, FILE_DIR, user.getId());
        final UploadView view = UploadView
            .builder()
            .extName(store.getExtName())
            .mediaType(store.getMediaType())
            .fileName(store.getFileName())
            .size(store.getSize())
            .url(String.format("/api/files/%s/%s", user.getId(), store.getFileName()))
            .build();
        return ApiResult.ok(view);
    }

    @ApiOperation("读取图片")
    @GetMapping("/{userId}/{filename:[a-zA-Z0-9]+}")
    public ResponseEntity<?> get(
        @PathVariable("userId") final String userId,
        @PathVariable("filename") final String filename
    ) {
        if (!this.storageService.exist(filename, FILE_DIR, userId)) {
            return ApiResult.notFound("指定图片不存在").build().toResponseEntity();
        }
        try {
            final Resource resource = this.storageService.load(filename, FILE_DIR, userId);
            final BufferedInputStream bis = new BufferedInputStream(resource.getInputStream());
            String contentType = Mime.media(bis, filename).toString();
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            return ResponseEntity.ok().header(CONTENT_TYPE, contentType).body(resource);
        } catch (final IOException e) {
            return ApiResult.error("获取图片异常").build().toResponseEntity();
        }
    }

    @ApiOperation("删除图片")
    @GetMapping("/{filename:[a-zA-Z0-9]+}")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> delete(
        @PathVariable("filename") final String filename,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        if (!this.storageService.exist(filename, FILE_DIR, user.getId())) {
            return ApiResult.bindException(new String[] { "图片不存在，无法删除" });
        }
        this.storageService.delete(filename, FILE_DIR, user.getId());
        return ApiResult.ok("删除成功").build();
    }
}
