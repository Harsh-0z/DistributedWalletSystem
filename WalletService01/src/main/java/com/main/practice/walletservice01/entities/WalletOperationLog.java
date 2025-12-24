package com.main.practice.walletservice01.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wallet_operation_log",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sagaTransactionId", "operation"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletOperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // comes from orchestrator
    private Long sagaTransactionId;

    @Enumerated(EnumType.STRING)
    private OperationType operation; // DEBIT, CREDIT, REFUND

    private String username;          // sender or receiver
    private BigDecimal amount;

    private LocalDateTime createdAt = LocalDateTime.now();


}
