/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.service;

import io.minio.*;
import io.minio.messages.Item;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.file.exception.MinioException;
import me.ixk.hoshi.file.exception.MinioFetchException;
import me.ixk.hoshi.file.properties.MinioProperties;
import org.springframework.stereotype.Service;

/**
 * Minio 服务
 *
 * @author Otstar Lin
 * @date 2021/11/14 21:12
 */
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient client;
    private final MinioProperties properties;

    public Stream<Item> list() {
        return list("");
    }

    public Stream<Item> list(final String path) {
        final ListObjectsArgs args = ListObjectsArgs
            .builder()
            .bucket(properties.getBucket())
            .prefix(path.toString())
            .recursive(true)
            .build();
        return getItems(client.listObjects(args));
    }

    public InputStream read(final String path) {
        try {
            final GetObjectArgs args = GetObjectArgs
                .builder()
                .bucket(properties.getBucket())
                .object(path.toString())
                .build();
            return client.getObject(args);
        } catch (Exception e) {
            throw new MinioException("Error while fetching files in Minio", e);
        }
    }

    public void upload(String path, InputStream stream, Map<String, String> headers) {
        try {
            PutObjectArgs args = PutObjectArgs
                .builder()
                .bucket(properties.getBucket())
                .object(path.toString())
                .stream(stream, stream.available(), -1)
                .headers(headers)
                .build();
            client.putObject(args);
        } catch (Exception e) {
            throw new MinioException("Error while fetching files in Minio", e);
        }
    }

    public void upload(String path, InputStream stream) {
        upload(path, stream, Map.of());
    }

    public StatObjectResponse stat(String path) {
        try {
            StatObjectArgs args = StatObjectArgs
                .builder()
                .bucket(properties.getBucket())
                .object(path.toString())
                .build();
            return client.statObject(args);
        } catch (Exception e) {
            throw new MinioException("Error while fetching files in Minio", e);
        }
    }

    public void remove(String path) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs
                .builder()
                .bucket(properties.getBucket())
                .object(path.toString())
                .build();
            client.removeObject(args);
        } catch (Exception e) {
            throw new MinioException("Error while fetching files in Minio", e);
        }
    }

    private Stream<Item> getItems(final Iterable<Result<Item>> objects) {
        return StreamSupport
            .stream(objects.spliterator(), true)
            .map(itemResult -> {
                try {
                    return itemResult.get();
                } catch (final Exception e) {
                    throw new MinioFetchException("Error while parsing list of objects", e);
                }
            });
    }
}
