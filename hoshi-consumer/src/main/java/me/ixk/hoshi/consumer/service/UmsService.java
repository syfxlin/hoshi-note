package me.ixk.hoshi.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:05
 */
@FeignClient(name = "hoshi-ums")
public interface UmsService {
    @GetMapping("/user")
    String user();
}
