package com.main.practice.walletservice01.repositories;

import com.main.practice.walletservice01.entities.OperationType;
import com.main.practice.walletservice01.entities.WalletOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletOperationLogRepo extends JpaRepository<WalletOperationLog, Long> {

    boolean existsBySagaTransactionIdAndOperation(Long sagaTransactionId, OperationType operation);
}