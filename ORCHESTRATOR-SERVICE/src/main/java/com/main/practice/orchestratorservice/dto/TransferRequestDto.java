package com.main.practice.orchestratorservice.dto;


import lombok.Data;

import java.math.BigDecimal;


@Data
public class TransferRequestDto {
    private String senderUsername;
    private String senderService;   // Values: "A" or "B"

    private String receiverUsername;
    private String receiverService; // Values: "A" or "B"

    private BigDecimal amount;
}