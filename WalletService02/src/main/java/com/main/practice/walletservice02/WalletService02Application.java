package com.main.practice.walletservice02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WalletService02Application {

    public static void main(String[] args) {
        SpringApplication.run(WalletService02Application.class, args);
    }

}
