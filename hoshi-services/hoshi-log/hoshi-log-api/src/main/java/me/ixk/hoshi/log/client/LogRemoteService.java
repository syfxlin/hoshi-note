package me.ixk.hoshi.log.client;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.log.view.request.AddLogView;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Otstar Lin
 * @date 2021/6/8 15:41
 */
@Service
@RequiredArgsConstructor
public class LogRemoteService {

    private final AmqpTemplate amqpTemplate;

    /**
     * 添加日志
     *
     * @param view 日志信息
     */
    public void add(final AddLogView view) {
        amqpTemplate.convertAndSend("log", view);
    }
}
