package com.main.practice.walletservice02.services;


import com.main.practice.walletservice02.dtos.UserAccountCreateReqDto;
import com.main.practice.walletservice02.entities.UserAccount;
import com.main.practice.walletservice02.mappers.UserAccountMapper;
import com.main.practice.walletservice02.repositories.IUserAccountRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final IUserAccountRepo userRepo;

    public UserAccount createAccount(UserAccountCreateReqDto request) {

        if(userRepo.existsByUsername(request.getUsername())){
                throw new RuntimeException("Username already taken");
        }

        UserAccount newUser = UserAccountMapper.toEntity(request);

        return userRepo.save(newUser);

    }


    @Transactional//keep the db connection open for the lock to work
    public void debitAccount(String username, BigDecimal amount) {

        UserAccount user = userRepo.findByUsernameAndLock(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));


        user.debit(amount);

        userRepo.save(user);
    }
    @Transactional
    public void creditAccount(String username, BigDecimal amount) {

        UserAccount user = userRepo.findByUsernameAndLock(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        user.credit(amount);

        userRepo.save(user);
    }




}
