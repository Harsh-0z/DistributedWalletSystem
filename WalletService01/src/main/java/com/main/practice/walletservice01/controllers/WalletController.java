package com.main.practice.walletservice01.controllers;


import com.main.practice.walletservice01.dtos.UserAccountCreateReqDto;
import com.main.practice.walletservice01.entities.UserAccount;
import com.main.practice.walletservice01.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final UserAccountService userService;

    @PostMapping("/create")
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccountCreateReqDto request) {
        UserAccount newUser = userService.createAccount(request);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestParam String username, @RequestParam BigDecimal amount, @RequestParam Long sagaTransactionId) {
        try {
            userService.debitAccount(username, amount, sagaTransactionId);
            return ResponseEntity.ok("Debit Successful");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Debit Failed: " + e.getMessage());
        }
    }

    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestParam String username, @RequestParam BigDecimal amount, @RequestParam Long sagaTransactionId) {
        try {
            userService.creditAccount(username, amount, sagaTransactionId);
            return ResponseEntity.ok("Credit Successful");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Credit Failed: " + e.getMessage());
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam String username, @RequestParam BigDecimal amount, @RequestParam Long sagaTransactionId) {
        try {
            userService.refundAccount(username, amount, sagaTransactionId);
            return ResponseEntity.ok("Refund Successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("CRITICAL: Refund Failed! " + e.getMessage());
        }
    }
}
