package me.ixk.hoshi.log.client;

import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.log.factory.LogFallbackFactory;
import me.ixk.hoshi.log.view.request.AddLogView;
import me.ixk.hoshi.log.view.response.LogView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Otstar Lin
 * @date 2021/6/8 15:41
 */
@FeignClient(name = "hoshi-log", fallbackFactory = LogFallbackFactory.class)
public interface LogFeignService {
    /**
     * 添加日志
     *
     * @param view 日志信息
     * @return 日志
     */
    @PostMapping("/api/log")
    ApiEntity<LogView> add(final AddLogView view);
}
