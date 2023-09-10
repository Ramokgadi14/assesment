package com.momentum.assesment.controller;

import com.momentum.assesment.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    // CustomExceptionHandler to handle all the custom exceptions i've created

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WithdrawalPercentageException.class)
    public Map<String, String> handleWithdrawalPercentageException(WithdrawalPercentageException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WithdrawalLimitExceededException.class)
    public Map<String, String> handleWithdrawalLimitExceededException(WithdrawalLimitExceededException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MinimumWithdrawalAmountException.class)
    public Map<String, String> handleMinimumWithdrawalAmountException(MinimumWithdrawalAmountException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MinimumRequiredAgeException.class)
    public Map<String, String> handleMinimumRequiredAgeException(MinimumRequiredAgeException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductNotFoundException.class)
    public Map<String, String> handleProductNotFoundException(ProductNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvestorNotFoundException.class)
    public Map<String, String> handleInvestorNotFoundException(InvestorNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Message", exception.getMessage());
        return map;
    }
}
