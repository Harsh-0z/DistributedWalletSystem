package com.main.practice.walletservice02.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountCreateReqDto {

    // You can also add Validation annotations here later (e.g., @NotBlank)
    private String username;
    private String password;
    private String email;
    private BigDecimal initialBalance;
}