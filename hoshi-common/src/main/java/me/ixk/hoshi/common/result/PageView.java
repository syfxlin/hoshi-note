package me.ixk.hoshi.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageView<T> {

    private Long page;
    private long pageSize = 15;
    private List<String> orderBy = new ArrayList<>();

    public IPage<T> toPage() {
        final Page<T> page = new Page<>(this.page, this.pageSize);
        page.addOrder(
            this.orderBy.stream()
                .map(
                    o -> {
                        final String[] split = o.split("\\.");
                        if (split.length == 1) {
                            return OrderItem.asc(split[0]);
                        }
                        return new OrderItem(split[0], "ASC".equals(split[1]));
                    }
                )
                .toArray(OrderItem[]::new)
        );
        return page;
    }
}
