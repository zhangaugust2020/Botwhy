package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author August
 * @create 2022-09-08 13:33
 */
@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingSystemApplication.class, args);
        MatchingServiceImpl.MATCHING_POOL.start();
    }
}
