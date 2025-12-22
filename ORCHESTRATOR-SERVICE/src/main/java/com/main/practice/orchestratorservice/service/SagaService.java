package com.main.practice.orchestratorservice.service;

import com.main.practice.orchestratorservice.clients.WalletAClient;
import com.main.practice.orchestratorservice.clients.WalletBClient;
import com.main.practice.orchestratorservice.dto.TransferRequestDto;
import com.main.practice.orchestratorservice.entities.SagaStatus;
import com.main.practice.orchestratorservice.entities.SagaTransaction;
import com.main.practice.orchestratorservice.repositories.SagaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SagaService {
    @Autowired
    private WalletAClient walletA;
    @Autowired
    private WalletBClient walletB;
    @Autowired private SagaTransactionRepository repo;

    public String processTransaction(TransferRequestDto request) {

        // 1. PERSIST STATE: Save the full DTO context to DB
        SagaTransaction tx = new SagaTransaction();
        tx.setSenderUsername(request.getSenderUsername());
        tx.setSenderService(request.getSenderService()); // Saved!
        tx.setReceiverUsername(request.getReceiverUsername());
        tx.setReceiverService(request.getReceiverService());
        tx.setAmount(request.getAmount().doubleValue());
        tx.setStatus(SagaStatus.STARTED);

        SagaTransaction savedTx = repo.save(tx); // <--- ID generated here

        try {
            // ... PHASE 1: DEBIT ...
            if ("A".equalsIgnoreCase(request.getSenderService())) {
                walletA.debit(request.getSenderUsername(), request.getAmount());
            } else {
                walletB.debit(request.getSenderUsername(), request.getAmount());
            }

            savedTx.setStatus(SagaStatus.DEBIT_COMPLETED);
            repo.save(savedTx);


            //Thread.sleep(15000);

        } catch (Exception e) {
            savedTx.setStatus(SagaStatus.FAILED);
            repo.save(savedTx);
            return "Failed at Debit.";
        }

        try {
            // ... PHASE 2: CREDIT ...
            if ("A".equalsIgnoreCase(request.getReceiverService())) {
                walletA.credit(request.getReceiverUsername(), request.getAmount());
            } else {
                walletB.credit(request.getReceiverUsername(), request.getAmount());
            }
            savedTx.setStatus(SagaStatus.COMPLETED);
            repo.save(savedTx);
            return "Transaction Successful!";

        } catch (Exception e) {
            // ... COMPENSATION ...
            System.out.println("Credit Failed. Rolling back...");

            // Refund logic calls the clients...
            performRefund(request);

            savedTx.setStatus(SagaStatus.REFUNDED);
            repo.save(savedTx);
            return "Transaction Failed: Rolled Back.";        }


    }
    private void performRefund(TransferRequestDto request) {
        if ("A".equalsIgnoreCase(request.getSenderService())) {
            walletA.refund(request.getSenderUsername(), request.getAmount());
        } else {
            walletB.refund(request.getSenderUsername(), request.getAmount());
        }
    }
}
