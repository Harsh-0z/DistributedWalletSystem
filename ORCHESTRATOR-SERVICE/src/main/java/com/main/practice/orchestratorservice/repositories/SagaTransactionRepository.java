package com.main.practice.orchestratorservice.repositories;

import com.main.practice.orchestratorservice.entities.SagaStatus;
import com.main.practice.orchestratorservice.entities.SagaTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SagaTransactionRepository extends JpaRepository<SagaTransaction, Long> {
    List<SagaTransaction> findByStatusAndCreatedAtBefore(SagaStatus sagaStatus, LocalDateTime twoMinutesAgo);
}
