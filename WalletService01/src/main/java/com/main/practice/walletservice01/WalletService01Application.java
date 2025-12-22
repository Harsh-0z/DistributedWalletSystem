package com.main.practice.walletservice01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WalletService01Application {

    public static void main(String[] args) {
        SpringApplication.run(WalletService01Application.class, args);
    }

}
