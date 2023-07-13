package com.sun.zq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: sunzheng
 * @date:
 * @description:
 */
@SpringBootApplication
@EnableScheduling
public class SpringbootSseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSseApplication.class, args);
    }

}
