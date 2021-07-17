package me.ixk.hoshi.mail;

import me.ixk.hoshi.client.annotation.EnableHoshiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Otstar Lin
 * @date 2021/7/17 16:18
 */
@SpringBootApplication
@EnableHoshiClient(basePackageClasses = HoshiMailApiAutoConfiguration.class)
public class HoshiMailApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiMailApplication.class, args);
    }
}
