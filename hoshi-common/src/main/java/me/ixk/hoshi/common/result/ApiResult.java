package me.ixk.hoshi.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletResponse;
import me.ixk.hoshi.common.util.Json;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

/**
 * 统一响应
 *
 * @author Otstar Lin
 * @date 2021/5/17 下午 9:46
 */
public class ApiResult<T> {

    private final Object status;

    @Nullable
    private final String message;

    private final HttpHeaders headers;
    private final T data;

    public ApiResult(final ApiMessage status) {
        this(status, null);
    }

    public ApiResult(final ApiMessage status, @Nullable final String message) {
        this(null, null, status, message);
    }

    public ApiResult(@Nullable final T data, final ApiMessage status, @Nullable final String message) {
        this(data, null, status, message);
    }

    public ApiResult(
        final MultiValueMap<String, String> headers,
        final ApiMessage status,
        @Nullable final String message
    ) {
        this(null, headers, status, message);
    }

    public ApiResult(
        @Nullable final T data,
        @Nullable final MultiValueMap<String, String> headers,
        final ApiMessage status,
        @Nullable final String message
    ) {
        this(data, headers, (Object) status, message);
    }

    public ApiResult(
        @Nullable final T data,
        @Nullable final MultiValueMap<String, String> headers,
        final int rawStatus,
        @Nullable final String message
    ) {
        this(data, headers, (Object) rawStatus, message);
    }

    private ApiResult(
        @Nullable final T data,
        @Nullable final MultiValueMap<String, String> headers,
        final Object status,
        @Nullable final String message
    ) {
        Assert.notNull(status, "Status must not be null");
        this.status = status;
        this.message = message;
        this.data = data;
        this.headers = HttpHeaders.readOnlyHttpHeaders(headers != null ? headers : new HttpHeaders());
    }

    public ApiMessage getStatusCode() {
        if (this.status instanceof ApiMessage) {
            return (ApiMessage) this.status;
        } else {
            return ApiMessage.valueOf((Integer) this.status);
        }
    }

    public int getStatusCodeValue() {
        if (this.status instanceof ApiMessage) {
            return ((ApiMessage) this.status).value();
        } else {
            return (Integer) this.status;
        }
    }

    public String getMessage() {
        return message == null ? getStatusCode().message() : message;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    @Nullable
    public T getData() {
        return this.data;
    }

    public boolean hasData() {
        return (this.data != null);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        final ApiResult<?> otherEntity = (ApiResult<?>) other;
        return (
            ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) &&
            ObjectUtils.nullSafeEquals(this.data, otherEntity.data) &&
            ObjectUtils.nullSafeEquals(this.status, otherEntity.status)
        );
    }

    @Override
    public int hashCode() {
        return (
            29 *
            (ObjectUtils.nullSafeHashCode(this.headers) * 29 + ObjectUtils.nullSafeHashCode(this.data)) +
            ObjectUtils.nullSafeHashCode(this.status)
        );
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("<");
        builder.append(this.status);
        if (this.status instanceof ApiMessage) {
            builder.append(' ');
            builder.append(((ApiMessage) this.status).message());
        }
        builder.append(',');
        final T data = getData();
        final HttpHeaders headers = getHeaders();
        if (data != null) {
            builder.append(data);
            builder.append(',');
        }
        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }

    public ApiEntity<T> toEntity() {
        return new ApiEntity<>(this.getStatusCodeValue(), this.getMessage(), this.getData());
    }

    public JsonNode toJsonNode() {
        return Json.convertToNode(this.toEntity());
    }

    public ResponseEntity<ApiEntity<T>> toResponseEntity() {
        return ResponseEntity.status(this.getStatusCodeValue()).headers(this.getHeaders()).body(this.toEntity());
    }

    public HttpServletResponse toResponse(final HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(this.getStatusCodeValue());
        this.getHeaders().forEach((key, value) -> value.forEach(v -> response.addHeader(key, v)));
        response.getWriter().write(this.toJsonNode().toString());
        return response;
    }

    // Static builder methods

    public static DataBuilder status(final ApiMessage status) {
        Assert.notNull(status, "Status must not be null");
        return new DefaultBuilder(status);
    }

    public static DataBuilder status(final ApiMessage status, @Nullable final String message) {
        return status(status).message(message);
    }

    public static DataBuilder status(final int status) {
        return new DefaultBuilder(status);
    }

    public static DataBuilder status(final int status, @Nullable final String message) {
        return status(status).message(message);
    }

    public static DataBuilder status(final HttpStatus status) {
        Assert.notNull(status, "Status must not be null");
        return status(status.value());
    }

    public static DataBuilder status(final HttpStatus status, @Nullable final String message) {
        return status(status).message(message);
    }

    public static DataBuilder ok() {
        return ok(null);
    }

    public static DataBuilder ok(@Nullable final String message) {
        return status(ApiMessage.OK, message);
    }

    public static <T> ApiResult<T> ok(@Nullable final T data) {
        return ok().data(data);
    }

    public static <T> ApiResult<T> ok(@Nullable final T data, @Nullable final String message) {
        return ok(message).data(data);
    }

    public static <T> ApiResult<ApiPage<T>> page(final List<T> records) {
        return page(records, null);
    }

    public static <T> ApiResult<ApiPage<T>> page(final List<T> records, @Nullable final String message) {
        return page(new ApiPage<>(records), message);
    }

    public static <T> ApiResult<ApiPage<T>> page(final IPage<T> page) {
        return page(page, null);
    }

    public static <T> ApiResult<ApiPage<T>> page(final IPage<T> page, @Nullable final String message) {
        return ok(message).page(page);
    }

    public static <T> ApiResult<ApiPage<T>> page(final ApiPage<T> page) {
        return page(page, null);
    }

    public static <T> ApiResult<ApiPage<T>> page(final ApiPage<T> page, @Nullable final String message) {
        return ok(message).page(page);
    }

    public static DataBuilder error(final String message) {
        Assert.notNull(message, "Message must be null");
        return status(ApiMessage.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> ApiResult<T> error(final T error) {
        Assert.notNull(error, "Error must not be null");
        return error(error, null);
    }

    public static <T> ApiResult<T> error(@Nullable final T error, @Nullable final String message) {
        return status(ApiMessage.INTERNAL_SERVER_ERROR, message).data(error);
    }

    public static DataBuilder created(final URI location) {
        return created(location, null);
    }

    public static DataBuilder created(final URI location, @Nullable final String message) {
        return status(ApiMessage.CREATED, message).location(location);
    }

    public static DataBuilder accepted() {
        return accepted(null);
    }

    public static DataBuilder accepted(@Nullable final String message) {
        return status(ApiMessage.ACCEPTED, message);
    }

    public static HeadersBuilder<?> noContent() {
        return noContent(null);
    }

    public static HeadersBuilder<?> noContent(@Nullable final String message) {
        return status(ApiMessage.NO_CONTENT, message);
    }

    public static DataBuilder badRequest() {
        return badRequest(null);
    }

    public static DataBuilder badRequest(@Nullable final String message) {
        return status(ApiMessage.BAD_REQUEST, message);
    }

    public static HeadersBuilder<?> notFound() {
        return notFound(null);
    }

    public static HeadersBuilder<?> notFound(@Nullable final String message) {
        return status(ApiMessage.NOT_FOUND, message);
    }

    public static DataBuilder unprocessableEntity() {
        return unprocessableEntity(null);
    }

    public static DataBuilder unprocessableEntity(@Nullable final String message) {
        return status(ApiMessage.UNPROCESSABLE_ENTITY, message);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        /**
         * Add the given, single header value under the given name.
         *
         * @param headerName   the header name
         * @param headerValues the header value(s)
         * @return this builder
         * @see HttpHeaders#add(String, String)
         */
        B header(String headerName, String... headerValues);

        /**
         * Copy the given headers into the entity's headers map.
         *
         * @param headers the existing HttpHeaders to copy from
         * @return this builder
         * @see HttpHeaders#add(String, String)
         * @since 4.1.2
         */
        B headers(@Nullable HttpHeaders headers);

        /**
         * Manipulate this entity's headers with the given consumer. The
         * headers provided to the consumer are "live", so that the consumer can be used to
         * {@linkplain HttpHeaders#set(String, String) overwrite} existing header values,
         * {@linkplain HttpHeaders#remove(Object) remove} values, or use any of the other
         * {@link HttpHeaders} methods.
         *
         * @param headersConsumer a function that consumes the {@code HttpHeaders}
         * @return this builder
         * @since 5.2
         */
        B headers(Consumer<HttpHeaders> headersConsumer);

        /**
         * Set the set of allowed {@link HttpMethod HTTP methods}, as specified
         * by the {@code Allow} header.
         *
         * @param allowedMethods the allowed methods
         * @return this builder
         * @see HttpHeaders#setAllow(Set)
         */
        B allow(HttpMethod... allowedMethods);

        /**
         * Set the entity tag of the data, as specified by the {@code ETag} header.
         *
         * @param etag the new entity tag
         * @return this builder
         * @see HttpHeaders#setETag(String)
         */
        B eTag(String etag);

        /**
         * Set the time the resource was last changed, as specified by the
         * {@code Last-Modified} header.
         *
         * @param lastModified the last modified date
         * @return this builder
         * @see HttpHeaders#setLastModified(ZonedDateTime)
         * @since 5.1.4
         */
        B lastModified(ZonedDateTime lastModified);

        /**
         * Set the time the resource was last changed, as specified by the
         * {@code Last-Modified} header.
         *
         * @param lastModified the last modified date
         * @return this builder
         * @see HttpHeaders#setLastModified(Instant)
         * @since 5.1.4
         */
        B lastModified(Instant lastModified);

        /**
         * Set the time the resource was last changed, as specified by the
         * {@code Last-Modified} header.
         * <p>The date should be specified as the number of milliseconds since
         * January 1, 1970 GMT.
         *
         * @param lastModified the last modified date
         * @return this builder
         * @see HttpHeaders#setLastModified(long)
         */
        B lastModified(long lastModified);

        /**
         * Set the location of a resource, as specified by the {@code Location} header.
         *
         * @param location the location
         * @return this builder
         * @see HttpHeaders#setLocation(URI)
         */
        B location(URI location);

        /**
         * Set the caching directives for the resource, as specified by the HTTP 1.1
         * {@code Cache-Control} header.
         * <p>A {@code CacheControl} instance can be built like
         * {@code CacheControl.maxAge(3600).cachePublic().noTransform()}.
         *
         * @param cacheControl a builder for cache-related HTTP response headers
         * @return this builder
         * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2">RFC-7234 Section 5.2</a>
         * @since 4.2
         */
        B cacheControl(CacheControl cacheControl);

        /**
         * Configure one or more request header names (e.g. "Accept-Language") to
         * add to the "Vary" response header to inform clients that the response is
         * subject to content negotiation and variances based on the value of the
         * given request headers. The configured request header names are added only
         * if not already present in the response "Vary" header.
         *
         * @param requestHeaders request header names
         * @since 4.3
         */
        B varyBy(String... requestHeaders);

        /**
         * Build the response entity with no data.
         *
         * @return the response entity
         * @see DataBuilder#data(Object)
         */
        <T> ApiResult<T> build();
    }

    public interface DataBuilder extends HeadersBuilder<DataBuilder> {
        DataBuilder contentLength(long contentLength);

        DataBuilder contentType(MediaType contentType);

        <T> ApiResult<T> data(@Nullable T data);

        DataBuilder message(@Nullable final String message);

        default <T> ApiResult<ApiPage<T>> page(final IPage<T> page) {
            return page(new ApiPage<>(page));
        }

        default <T> ApiResult<ApiPage<T>> page(final ApiPage<T> page) {
            return data(page);
        }
    }

    private static class DefaultBuilder implements DataBuilder {

        private final Object statusCode;
        private final HttpHeaders headers = new HttpHeaders();

        @Nullable
        private String message;

        public DefaultBuilder(final Object statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public DataBuilder message(@Nullable final String message) {
            this.message = message;
            return this;
        }

        @Override
        public DataBuilder header(final String headerName, final String... headerValues) {
            for (final String headerValue : headerValues) {
                this.headers.add(headerName, headerValue);
            }
            return this;
        }

        @Override
        public DataBuilder headers(@Nullable final HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }
            return this;
        }

        @Override
        public DataBuilder headers(final Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        @Override
        public DataBuilder allow(final HttpMethod... allowedMethods) {
            this.headers.setAllow(new LinkedHashSet<>(Arrays.asList(allowedMethods)));
            return this;
        }

        @Override
        public DataBuilder contentLength(final long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        @Override
        public DataBuilder contentType(final MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        @Override
        public DataBuilder eTag(String etag) {
            if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
                etag = "\"" + etag;
            }
            if (!etag.endsWith("\"")) {
                etag = etag + "\"";
            }
            this.headers.setETag(etag);
            return this;
        }

        @Override
        public DataBuilder lastModified(final ZonedDateTime date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public DataBuilder lastModified(final Instant date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public DataBuilder lastModified(final long date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public DataBuilder location(final URI location) {
            this.headers.setLocation(location);
            return this;
        }

        @Override
        public DataBuilder cacheControl(final CacheControl cacheControl) {
            this.headers.setCacheControl(cacheControl);
            return this;
        }

        @Override
        public DataBuilder varyBy(final String... requestHeaders) {
            this.headers.setVary(Arrays.asList(requestHeaders));
            return this;
        }

        @Override
        public <T> ApiResult<T> build() {
            return data(null);
        }

        @Override
        public <T> ApiResult<T> data(@Nullable final T data) {
            return new ApiResult<>(data, this.headers, this.statusCode, this.message);
        }
    }
}
