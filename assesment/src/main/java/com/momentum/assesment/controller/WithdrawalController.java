package com.momentum.assesment.controller;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.exception.*;
import com.momentum.assesment.service.InvestorService;
import com.momentum.assesment.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/withdrawals")
@Validated
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private InvestorService investorService;

    //methods for pull all withdrawal requests  by email
    // creating requests by passing email and withdrawal amount

    @PostMapping("/request-retirement-withdrawal/{email}/{withdrawalAmount}")
    public ResponseEntity<Map<String, String>> requestRetirementWithdrawal(
            @PathVariable("email") String email,
            @PathVariable("withdrawalAmount") double withdrawalAmount) throws InvestorNotFoundException, WithdrawalLimitExceededException, MinimumWithdrawalAmountException,
            ProductNotFoundException, MinimumRequiredAgeException {

        Investor investor = investorService.getInvestorByEmail(email);
        withdrawalService.requestWithdrawal(investor, ProductName.RETIREMENT, withdrawalAmount);
        Map<String, String> map = new HashMap<>();
        map.put("Message", "Withdrawal request sent successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/request-savings-withdrawal/{email}/{withdrawalAmount}")
    public ResponseEntity<Map<String, String>> requestSavingsWithdrawal(
            @PathVariable("email") @Email(message = "Email should be valid")
            String email, @PathVariable double withdrawalAmount) throws InvestorNotFoundException, WithdrawalLimitExceededException, MinimumWithdrawalAmountException,
            ProductNotFoundException, MinimumRequiredAgeException {

        Investor investor = investorService.getInvestorByEmail(email);
        withdrawalService.requestWithdrawal(investor, ProductName.SAVINGS, withdrawalAmount);
        Map<String, String> map = new HashMap<>();
        map.put("Message", "Withdrawal request sent successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/withdrawals/{email})")
    public ResponseEntity<List<Withdrawal>> getWithdrawalsByMail(@PathVariable("email") String email) throws InvestorNotFoundException {

        Investor investor = investorService.getInvestorByEmail(email);
        return new ResponseEntity<>(withdrawalService.getWithdrawalByInvestorId(investor.getId()), HttpStatus.OK);
    }

}
