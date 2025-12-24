package com.main.practice.walletservice01.services;


import com.main.practice.walletservice01.dtos.UserAccountCreateReqDto;
import com.main.practice.walletservice01.entities.OperationType;
import com.main.practice.walletservice01.entities.UserAccount;
import com.main.practice.walletservice01.entities.WalletOperationLog;
import com.main.practice.walletservice01.mappers.UserAccountMapper;
import com.main.practice.walletservice01.repositories.IUserAccountRepo;
import com.main.practice.walletservice01.repositories.WalletOperationLogRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final IUserAccountRepo userRepo;
    private final WalletOperationLogRepo operationLogRepo;

    public UserAccount createAccount(UserAccountCreateReqDto request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        UserAccount newUser = UserAccountMapper.toEntity(request);
        return userRepo.save(newUser);
    }

    @Transactional
    public void debitAccount(
            String username,
            BigDecimal amount,
            Long sagaTransactionId
    ) {

        if (operationLogRepo.existsBySagaTransactionIdAndOperation(
                sagaTransactionId, OperationType.DEBIT)) {
            return;
        }

        UserAccount acc = userRepo.findByUsernameAndLock(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        acc.debit(amount);
        userRepo.save(acc);

        operationLogRepo.save(
                WalletOperationLog.builder()
                        .sagaTransactionId(sagaTransactionId)
                        .operation(OperationType.DEBIT)
                        .username(username)
                        .amount(amount)
                        .build()
        );
    }

    @Transactional
    public void creditAccount(
            String username,
            BigDecimal amount,
            Long sagaTransactionId
    ) {
        if (operationLogRepo.existsBySagaTransactionIdAndOperation(
                sagaTransactionId, OperationType.CREDIT)) {
            return;
        }

        UserAccount acc = userRepo.findByUsernameAndLock(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        acc.credit(amount);
        userRepo.save(acc);

        operationLogRepo.save(
                WalletOperationLog.builder()
                        .sagaTransactionId(sagaTransactionId)
                        .operation(OperationType.CREDIT)
                        .username(username)
                        .amount(amount)
                        .build()
        );
    }

    @Transactional
    public void refundAccount(
            String username,
            BigDecimal amount,
            Long sagaTransactionId
    ) {
        if (operationLogRepo.existsBySagaTransactionIdAndOperation(
                sagaTransactionId, OperationType.REFUND)) {
            return;
        }

        UserAccount acc = userRepo.findByUsernameAndLock(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        acc.credit(amount);
        userRepo.save(acc);

        operationLogRepo.save(
                WalletOperationLog.builder()
                        .sagaTransactionId(sagaTransactionId)
                        .operation(OperationType.REFUND)
                        .username(username)
                        .amount(amount)
                        .build()
        );
    }
}
