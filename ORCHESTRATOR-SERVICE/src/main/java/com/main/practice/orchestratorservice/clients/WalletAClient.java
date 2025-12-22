package com.main.practice.orchestratorservice.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "WalletService01")
public interface WalletAClient {

    // Used when A is the SENDER
    @PostMapping("/api/wallet/debit")
    ResponseEntity<String> debit(@RequestParam("username") String username,
                                 @RequestParam("amount") BigDecimal amount);

    // Used when A is the RECEIVER
    @PostMapping("/api/wallet/credit")
    ResponseEntity<String> credit(@RequestParam("username") String username,
                                  @RequestParam("amount") BigDecimal amount);

    // Used when A was the Sender, but B failed (Rollback)
    @PostMapping("/api/wallet/refund")
    ResponseEntity<String> refund(@RequestParam("username") String username,
                                  @RequestParam("amount") BigDecimal amount);
}