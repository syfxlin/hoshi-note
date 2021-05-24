package me.ixk.hoshi.file.controller;

import static me.ixk.hoshi.security.util.Security.USER_ATTR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.file.exception.StorageException;
import me.ixk.hoshi.file.service.StorageService;
import me.ixk.hoshi.file.service.StorageService.StoreInfo;
import me.ixk.hoshi.file.view.FileView;
import me.ixk.hoshi.file.view.UploadView;
import me.ixk.hoshi.user.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:08
 */
@RestController
@RequestMapping("/api/files")
@Api("文件控制器")
@RequiredArgsConstructor
public class FileController {

    public static final String FILE_DIR = "user-file";
    private final StorageService storageService;

    @ApiOperation("列出文件")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<List<FileView>> list(final PageView vo, @ModelAttribute(USER_ATTR) final User user) {
        Stream<Resource> all = this.storageService.loadAll(FILE_DIR, user.getId().toString());
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
                                .url("/api/files/" + r.getFilename())
                                .build();
                        } catch (final IOException e) {
                            throw new StorageException("获取文件大小失败", e);
                        }
                    }
                )
                .collect(Collectors.toList())
        );
    }

    @ApiOperation("上传文件")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResult<UploadView> upload(
        @RequestParam("file") final MultipartFile file,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        final StoreInfo store = this.storageService.store(file, FILE_DIR, user.getId().toString());
        final UploadView view = UploadView
            .builder()
            .extName(store.getExtName())
            .mediaType(store.getMediaType())
            .fileName(store.getFileName())
            .size(store.getSize())
            .url("/api/files/" + store.getFileName())
            .build();
        return ApiResult.ok(view);
    }

    @ApiOperation("下载文件")
    @GetMapping("/{filename:.+\\.[a-zA-Z]+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> get(
        @PathVariable("filename") final String filename,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        if (!this.storageService.exist(filename, FILE_DIR, user.getId().toString())) {
            return ApiResult.notFound("指定文件不存在").build().toResponseEntity();
        }
        try {
            final Resource resource = this.storageService.load(filename, FILE_DIR, user.getId().toString());
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", filename));
            headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(resource.contentLength());
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (final IOException e) {
            return ApiResult.error("获取文件异常").build().toResponseEntity();
        }
    }
}
