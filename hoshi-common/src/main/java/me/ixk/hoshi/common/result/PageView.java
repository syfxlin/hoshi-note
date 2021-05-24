package me.ixk.hoshi.common.result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageView {

    private Integer page;
    private int pageSize = 15;
    private List<String> orderBy = new ArrayList<>();

    public Pageable toPage() {
        final Sort sort = Sort.by(
            this.orderBy.stream()
                .map(
                    o -> {
                        final String[] split = o.split(":");
                        if (split.length == 1 || "ASC".equalsIgnoreCase(split[1])) {
                            return Sort.Order.asc(split[0]);
                        } else {
                            return Sort.Order.desc(split[1]);
                        }
                    }
                )
                .collect(Collectors.toList())
        );
        return PageRequest.of(this.page, this.pageSize, sort);
    }
}
