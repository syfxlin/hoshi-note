package me.ixk.hoshi.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:33
 */
@Data
@RequiredArgsConstructor
public class ApiEntity<T> {

    private final int code;
    private final String msg;
    private final OffsetDateTime time = OffsetDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
}
