package me.ixk.hoshi.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Otstar Lin
 * @date 2022/2/4 上午 02:04
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final DiscoveryClient client;

    @GetMapping("/services")
    public List<String> getServiceList() {

        return client.getServices();
    }

    @GetMapping("/instance")
    public Object getInstance(@RequestParam("name") String name) {
        return client.getInstances(name);
    }
}
