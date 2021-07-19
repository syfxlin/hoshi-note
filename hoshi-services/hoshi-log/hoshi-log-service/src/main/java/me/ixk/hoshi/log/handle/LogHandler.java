package me.ixk.hoshi.log.handle;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.log.entity.Log;
import me.ixk.hoshi.log.repository.LogRepository;
import me.ixk.hoshi.log.view.request.AddLogView;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/7/18 22:16
 */
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "log")
public class LogHandler {

    private final LogRepository logRepository;

    @RabbitHandler
    public void add(final AddLogView log) {
        logRepository.save(Log.ofAdd(log));
    }
}
