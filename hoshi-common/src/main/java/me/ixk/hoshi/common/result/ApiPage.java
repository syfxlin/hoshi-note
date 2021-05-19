package me.ixk.hoshi.common.result;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPage<T> {

    private long page;
    private long pageSize;
    private long pages;
    private long total;
    private List<T> records;

    public ApiPage(@NotNull final Page<T> page) {
        this(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), page.getContent());
    }

    public ApiPage(@NotNull final List<T> records) {
        this(1, records.size(), 1, records.size(), records);
    }
}
