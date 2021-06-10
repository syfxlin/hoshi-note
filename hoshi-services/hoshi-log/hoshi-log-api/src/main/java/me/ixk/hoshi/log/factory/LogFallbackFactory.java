package me.ixk.hoshi.log.factory;

import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.log.client.LogFeignService;
import me.ixk.hoshi.log.view.request.AddLogView;
import me.ixk.hoshi.log.view.response.LogView;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/6/8 15:53
 */
@Slf4j
@Component
public class LogFallbackFactory implements FallbackFactory<LogFeignService> {

    @Override
    public LogFeignService create(final Throwable cause) {
        if (log.isErrorEnabled()) {
            log.info("日志服务调用失败：{}", cause.getMessage());
        }
        return new LogFeignService() {
            @Override
            public ApiEntity<LogView> add(final AddLogView view) {
                return null;
            }
        };
    }
}
