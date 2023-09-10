package com.momentum.assesment.exception;

public class WithdrawalLimitExceededException extends Exception {
    public WithdrawalLimitExceededException(String errorMessage) {
        super(errorMessage);
    }
}
