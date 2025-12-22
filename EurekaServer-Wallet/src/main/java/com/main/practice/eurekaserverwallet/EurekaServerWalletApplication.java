package com.main.practice.eurekaserverwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerWalletApplication.class, args);
    }

}
