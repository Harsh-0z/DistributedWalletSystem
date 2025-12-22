package com.main.practice.orchestratorservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name="WalletService02")
public interface WalletBClient {

    // Used when B is the SENDER
    @PostMapping("/api/wallet/debit")
    ResponseEntity<String> debit(@RequestParam("username") String username,
                                 @RequestParam("amount") BigDecimal amount);

    // Used when B is the RECEIVER
    @PostMapping("/api/wallet/credit")
    ResponseEntity<String> credit(@RequestParam("username") String username,
                                  @RequestParam("amount") BigDecimal amount);

    // Used when B was the Sender, but A failed (Rollback)
    @PostMapping("/api/wallet/refund")
    ResponseEntity<String> refund(@RequestParam("username") String username,
                                  @RequestParam("amount") BigDecimal amount);
}