package me.ixk.hoshi.file.controller;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.file.service.StorageService;
import me.ixk.hoshi.file.service.StorageService.StoreInfo;
import me.ixk.hoshi.file.util.Mime;
import me.ixk.hoshi.file.view.UploadView;
import me.ixk.hoshi.user.entity.User;
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

    @ApiOperation("上传图片")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResult<UploadView> upload(
        @RequestParam("file") final MultipartFile file,
        @ModelAttribute final User user
    ) {
        final StoreInfo store = this.storageService.store(file, FILE_DIR, user.getId().toString());
        final UploadView view = UploadView
            .builder()
            .extName(store.getExtName())
            .mediaType(store.getMediaType())
            .fileName(store.getFileName())
            .size(store.getSize())
            .url("/api/images/" + store.getFileName())
            .build();
        return ApiResult.ok(view);
    }

    @ApiOperation("读取图片")
    @GetMapping("/{filename:.+\\.[a-zA-Z]+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> get(@PathVariable("filename") final String filename, @ModelAttribute final User user) {
        if (!this.storageService.exist(filename, FILE_DIR, user.getId().toString())) {
            return ApiResult.notFound("指定图片不存在").build().toResponseEntity();
        }
        try {
            final Resource resource = this.storageService.load(filename, FILE_DIR, user.getId().toString());
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
}
