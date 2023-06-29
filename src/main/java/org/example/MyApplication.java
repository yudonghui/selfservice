package org.example;

import org.example.controller.VersionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApplication {
    private final static Logger logger = LoggerFactory.getLogger(VersionController.class);

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        logger.info("启动完成！");
    }
}
