package com.main.practice.walletservice02.mappers;

import com.main.practice.walletservice02.dtos.UserAccountCreateReqDto;
import com.main.practice.walletservice02.entities.UserAccount;

import java.math.BigDecimal;


public class UserAccountMapper {

    private UserAccountMapper() {
        throw new IllegalStateException("Utility class");
    }
    public static UserAccount toEntity(UserAccountCreateReqDto dto){
        if(dto == null){
            return null;
        }
        return UserAccount.builder().
                username(dto.getUsername()).
                password(dto.getPassword()).
                email(dto.getEmail()).
                balance(dto.getInitialBalance() != null ? dto.getInitialBalance() : BigDecimal.ZERO).
                build();


    }


}
