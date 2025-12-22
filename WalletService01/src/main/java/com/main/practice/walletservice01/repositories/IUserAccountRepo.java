package com.main.practice.walletservice01.repositories;

import com.main.practice.walletservice01.entities.UserAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserAccountRepo extends JpaRepository<UserAccount, Long> {

    //for viewing
    Optional<UserAccount> findByUsername(String username);


    // for debit and credit
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserAccount u WHERE u.username = :username")
    Optional<UserAccount> findByUsernameAndLock(@Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserAccount> findById(Integer id);

    boolean existsByUsername(String username);
}
