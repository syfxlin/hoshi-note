package me.ixk.hoshi.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class HoshiAdminApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiAdminApplication.class, args);
    }
}
