package com.main.practice.orchestratorservice.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "WalletService01")
public interface WalletAClient {

    @PostMapping("/api/wallet/debit")
    void debit(@RequestParam String username,
               @RequestParam BigDecimal amount,
               @RequestParam Long sagaTransactionId);

    @PostMapping("/api/wallet/credit")
    void credit(@RequestParam String username,
                @RequestParam BigDecimal amount,
                @RequestParam Long sagaTransactionId);

    @PostMapping("/api/wallet/refund")
    void refund(@RequestParam String username,
                @RequestParam BigDecimal amount,
                @RequestParam Long sagaTransactionId);
}