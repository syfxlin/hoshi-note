package me.ixk.hoshi.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

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
    private long offset;
    private List<T> records;

    public ApiPage(@NotNull final IPage<T> page) {
        this(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), page.offset(), page.getRecords());
    }

    public ApiPage(@NotNull final List<T> records) {
        this(1, records.size(), 1, records.size(), 0, records);
    }
}
