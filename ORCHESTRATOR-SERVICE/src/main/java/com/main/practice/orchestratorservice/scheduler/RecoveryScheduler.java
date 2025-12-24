package com.main.practice.orchestratorservice.scheduler;

import com.main.practice.orchestratorservice.clients.WalletAClient;
import com.main.practice.orchestratorservice.clients.WalletBClient;
import com.main.practice.orchestratorservice.entities.SagaStatus;
import com.main.practice.orchestratorservice.entities.SagaTransaction;
import com.main.practice.orchestratorservice.repositories.SagaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecoveryScheduler {

    @Autowired
    private SagaTransactionRepository repo;
    @Autowired
    private WalletAClient walletA;
    @Autowired
    private WalletBClient walletB;

    @Scheduled(fixedRate = 60000)
    public void recoverStuckTransactions() {
        List<SagaTransaction> stuckTx = repo.findByStatusAndCreatedAtBefore(
                SagaStatus.DEBIT_COMPLETED, LocalDateTime.now().minusMinutes(2)
        );

        for (SagaTransaction tx : stuckTx) {
            try {
                // DIRECTLY use the saved info. No lookup needed.
                if ("A".equalsIgnoreCase(tx.getSenderService())) {
                    walletA.refund(tx.getSenderUsername(), (tx.getAmount()), tx.getId());
                } else {
                    walletB.refund(tx.getSenderUsername(), (tx.getAmount()), tx.getId());
                }

                tx.setStatus(SagaStatus.REFUNDED);
                repo.save(tx);
                System.out.println("Recovered Transaction ID: " + tx.getId());

            } catch (Exception e) {
                System.err.println("Recovery failed for ID: " + tx.getId());
            }
        }
    }



}
