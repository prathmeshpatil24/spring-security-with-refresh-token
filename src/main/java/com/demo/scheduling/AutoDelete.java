package com.demo.scheduling;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoDelete {


    @Scheduled(cron = "0 0 3 * * *") // Runs every day at 3 AM
    public void autoDeleteRefreshTokens(){
        System.out.println("Auto delete refresh tokens executed");
    }
}
