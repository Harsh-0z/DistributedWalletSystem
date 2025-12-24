package com.main.practice.orchestratorservice.service;

import com.main.practice.orchestratorservice.clients.WalletAClient;
import com.main.practice.orchestratorservice.clients.WalletBClient;
import com.main.practice.orchestratorservice.dto.TransferRequestDto;
import com.main.practice.orchestratorservice.entities.SagaStatus;
import com.main.practice.orchestratorservice.entities.SagaTransaction;
import com.main.practice.orchestratorservice.repositories.SagaTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SagaService {

    private final WalletAClient walletA;

    private final WalletBClient walletB;


    private final SagaTransactionRepository repo;

    public String processTransaction(TransferRequestDto request) {

        SagaTransaction tx = new SagaTransaction();
        tx.setSenderUsername(request.getSenderUsername());
        tx.setSenderService(request.getSenderService());
        tx.setReceiverUsername(request.getReceiverUsername());
        tx.setReceiverService(request.getReceiverService());
        tx.setAmount(request.getAmount());
        tx.setStatus(SagaStatus.STARTED);

        SagaTransaction savedTx = repo.save(tx);

        try {
            debitSender(savedTx);
            savedTx.setStatus(SagaStatus.DEBIT_COMPLETED);
            repo.save(savedTx);
        } catch (Exception e) {
            savedTx.setStatus(SagaStatus.FAILED);
            repo.save(savedTx);
            return "Failed at Debit";
        }

        try {
            creditReceiver(savedTx);
            savedTx.setStatus(SagaStatus.COMPLETED);
            repo.save(savedTx);
            return "Transaction Successful";
        } catch (Exception e) {
            refundSender(savedTx);
            savedTx.setStatus(SagaStatus.REFUNDED);
            repo.save(savedTx);
            return "Transaction Failed: Rolled Back";
        }
    }

    private void debitSender(SagaTransaction tx) {
        if ("A".equalsIgnoreCase(tx.getSenderService())) {
            walletA.debit(
                    tx.getSenderUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        } else {
            walletB.debit(
                    tx.getSenderUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        }
    }

    private void creditReceiver(SagaTransaction tx) {
        if ("A".equalsIgnoreCase(tx.getReceiverService())) {
            walletA.credit(
                    tx.getReceiverUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        } else {
            walletB.credit(
                    tx.getReceiverUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        }
    }

    private void refundSender(SagaTransaction tx) {
        if ("A".equalsIgnoreCase(tx.getSenderService())) {
            walletA.refund(
                    tx.getSenderUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        } else {
            walletB.refund(
                    tx.getSenderUsername(),
                    (tx.getAmount()),
                    tx.getId()
            );
        }
    }
}
