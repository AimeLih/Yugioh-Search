package com.aimestart.yugiohsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class YugiohSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(YugiohSearchApplication.class, args);
    }

}
