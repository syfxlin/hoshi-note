package me.ixk.hoshi.log.client;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.log.view.request.AddLogView;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/6/8 17:41
 */
@Component
@RequiredArgsConstructor
public class AsyncLogFeignService {

    private final LogFeignService logFeignService;

    @Async
    public void add(final AddLogView view) {
        logFeignService.add(view);
    }
}
