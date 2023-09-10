package com.momentum.assesment.controller;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Product;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import com.momentum.assesment.exception.*;
import com.momentum.assesment.service.InvestorService;
import com.momentum.assesment.service.WithdrawalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WithdrawalControllerTest {
    @InjectMocks
    private WithdrawalController withdrawalController;

    @Mock
    private WithdrawalService withdrawalService;

    @Mock
    private InvestorService investorService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void requestRetirementWithdrawal() throws InvestorNotFoundException, WithdrawalLimitExceededException, MinimumWithdrawalAmountException, ProductNotFoundException, MinimumRequiredAgeException {
        Withdrawal withdrawal = createWithdrawal();
        when(investorService.getInvestorByEmail(withdrawal.getInvestor().getEmail())).thenReturn(withdrawal.getInvestor());

        ResponseEntity<Map<String, String>> responseEntity = withdrawalController.requestRetirementWithdrawal(withdrawal.getInvestor().getEmail(), withdrawal.getWithdrawalAmount());

        verify(withdrawalService, times(1)).requestWithdrawal(withdrawal.getInvestor(), ProductName.RETIREMENT, withdrawal.getWithdrawalAmount());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, String> responseBody = responseEntity.getBody();
        assertEquals("Withdrawal request sent successfully", responseBody.get("Message"));
    }

    @Test
    void requestSavingsWithdrawal() throws InvestorNotFoundException, WithdrawalLimitExceededException, MinimumWithdrawalAmountException, ProductNotFoundException, MinimumRequiredAgeException {
        Withdrawal withdrawal = createWithdrawal();
        withdrawal.getProduct().setName(ProductName.SAVINGS);
        when(investorService.getInvestorByEmail(withdrawal.getInvestor().getEmail())).thenReturn(withdrawal.getInvestor());

        ResponseEntity<Map<String, String>> responseEntity = withdrawalController.requestSavingsWithdrawal(withdrawal.getInvestor().getEmail(), withdrawal.getWithdrawalAmount());

        verify(withdrawalService, times(1)).requestWithdrawal(withdrawal.getInvestor(), ProductName.SAVINGS, withdrawal.getWithdrawalAmount());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, String> responseBody = responseEntity.getBody();
        assertEquals("Withdrawal request sent successfully", responseBody.get("Message"));
    }



    @Test
    public void testGetWithdrawalsByMail_Success() throws InvestorNotFoundException {
        Withdrawal withdrawal = createWithdrawal();

        when(investorService.getInvestorByEmail(withdrawal.getInvestor().getEmail())).thenReturn(withdrawal.getInvestor());

        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);
        when(withdrawalService.getWithdrawalByInvestorId(withdrawal.getInvestor().getId())).thenReturn(withdrawals);
        ResponseEntity<List<Withdrawal>> responseEntity = withdrawalController.getWithdrawalsByMail(withdrawal.getInvestor().getEmail());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(withdrawals, responseEntity.getBody());
    }

    private Withdrawal createWithdrawal()
    {
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");
        investor.setDateOfBirth(Date.valueOf(LocalDate.now()));

        Product product = new Product();
        product.setBalance(20.00);
        product.setName(ProductName.RETIREMENT);
        product.setId(1L);
        product.setInvestor(investor);

        investor.setProducts(Collections.singletonList(product));

        com.momentum.assesment.entities.Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setWithdrawalAmount(2.00);
        withdrawal.setProduct(product);
        withdrawal.setStatus(WithdrawalStatus.STARTED);
        withdrawal.setInvestor(investor);

        return  withdrawal;
    }
}