package com.main.practice.orchestratorservice.entities;

public enum SagaStatus {
    STARTED,
    DEBIT_COMPLETED, // Money taken from Sender
    COMPLETED,       // Success (Money given to Receiver)
    FAILED,          // Something went wrong
    REFUNDED         // Rolled back successfully
}