package com.main.practice.orchestratorservice.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class SagaTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- SENDER INFO (Needed for Refund) ---
    private String senderUsername;
    private String senderService;   // "A" or "B"

    // --- RECEIVER INFO (Needed for History/Retry) ---
    private String receiverUsername;
    private String receiverService; // "A" or "B"

    private Double amount;

    @Enumerated(EnumType.STRING)
    private SagaStatus status; // STARTED, DEBIT_COMPLETED, FAILED, REFUNDED, COMPLETED

    private LocalDateTime createdAt = LocalDateTime.now();


}