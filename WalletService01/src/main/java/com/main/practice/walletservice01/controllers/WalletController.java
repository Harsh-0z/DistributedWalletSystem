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


    //crate user
    @PostMapping("/create")
    public ResponseEntity<UserAccount> createUser(@RequestBody UserAccountCreateReqDto request) {
        UserAccount newUser = userService.createAccount(request);
        return ResponseEntity.ok(newUser);
    }

    //debit if failed rais 400
    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestParam String username, @RequestParam BigDecimal amount) {
        try {
            userService.debitAccount(username, amount);
            return ResponseEntity.ok("Debit Successful");
        } catch (Exception e) {
            // Return 400 or 500 so the Orchestrator knows it failed
            return ResponseEntity.status(400).body("Debit Failed: " + e.getMessage());
        }
    }

    //credit
    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestParam String username, @RequestParam BigDecimal amount) {
        try {
            userService.creditAccount(username, amount);
            return ResponseEntity.ok("Credit Successful");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Credit Failed: " + e.getMessage());
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam String username, @RequestParam BigDecimal amount) {
        // A refund is logically just a "Credit" back to the sender
        try {
            userService.creditAccount(username, amount);
            return ResponseEntity.ok("Refund/Rollback Successful");
        } catch (Exception e) {
            // If this fails, we have a "Zombie Transaction" (needs manual fix)
            return ResponseEntity.status(500).body("CRITICAL: Refund Failed! " + e.getMessage());
        }
    }

}
