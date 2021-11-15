/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.controller;

import cn.hutool.core.io.file.FileNameUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.file.entity.File;
import me.ixk.hoshi.file.repository.FileRepository;
import me.ixk.hoshi.file.response.FileView;
import me.ixk.hoshi.file.service.MinioService;
import me.ixk.hoshi.web.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器
 *
 * @author Otstar Lin
 * @date 2021/11/14 20:34
 */
@RestController
@RequiredArgsConstructor
@Api("文件控制器")
@RequestMapping("/files")
@Slf4j
public class FileController {

    private final FileRepository fileRepository;
    private final MinioService minioService;

    @ApiOperation("列出文件")
    @GetMapping("")
    @PreAuthorize("hasRole('FILE')")
    public ApiResult<ApiPage<File>> list(
        @UserId final Long userId,
        @RequestParam(value = "search", required = false) final String search,
        final Pageable page
    ) {
        final Specification<File> specification = (root, query, cb) ->
            search == null
                ? cb.equal(root.get("userId"), userId)
                : cb.and(
                    cb.equal(root.get("userId"), userId),
                    cb.or(
                        cb.like(root.get("name"), String.format("%%%s%%", search)),
                        cb.like(root.get("description"), String.format("%%%s%%", search))
                    )
                );
        return ApiResult.page(fileRepository.findAll(specification, page), "获取文件列表成功");
    }

    //    public ApiResult<Object> list(@UserId final Long userId, final Pageable page) {
    //        Stream<Item> stream = minioService
    //            .list(Path.of(String.format("user_%s", userId)))
    //            .filter(item -> !item.isDir());
    //        if (page.isPaged()) {
    //            stream = stream.skip(page.getOffset()).limit(page.getPageSize());
    //        }
    //        final List<FileView> list = stream
    //            .map(item -> {
    //                final String name = Path.of(item.objectName()).getFileName().toString();
    //                return FileView
    //                    .builder()
    //                    .name(name)
    //                    .size(item.size())
    //                    .url(String.format("/files/%s/%s", userId, name))
    //                    .build();
    //            })
    //            .collect(Collectors.toList());
    //        return ApiResult.ok(list, "获取所有文件信息成功");
    //    }

    @ApiOperation("上传文件")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FILE')")
    public ApiResult<FileView> upload(@UserId final Long userId, @RequestParam("file") final MultipartFile file) {
        final String filename = UUID.randomUUID().toString();
        final String extname = FileNameUtil.extName(file.getOriginalFilename());
        final String name = String.format("%s.%s", filename, extname);
        try {
            minioService.upload(String.format("%s/%s", userId.toString(), name), file.getInputStream());
            File entity = fileRepository.save(
                File
                    .builder()
                    .userId(userId)
                    .disk(name)
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .uploadedTime(OffsetDateTime.now())
                    .build()
            );
            return ApiResult.ok(entity.toView(), "上传成功");
        } catch (final Exception e) {
            log.error("上传失败", e);
            return ApiResult.error("上传失败").build();
        }
    }
}
