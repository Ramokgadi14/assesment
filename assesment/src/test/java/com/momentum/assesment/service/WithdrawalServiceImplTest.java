package com.momentum.assesment.service;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Product;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import com.momentum.assesment.exception.MinimumRequiredAgeException;
import com.momentum.assesment.exception.MinimumWithdrawalAmountException;
import com.momentum.assesment.exception.ProductNotFoundException;
import com.momentum.assesment.exception.WithdrawalLimitExceededException;
import com.momentum.assesment.repository.WithdrawalRepository;
import com.momentum.assesment.service.event.EventPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WithdrawalServiceImplTest {

    @InjectMocks
    private WithdrawalServiceImpl withdrawalService;

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private EventPublisherService publisherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        withdrawalService.setPublisherService(publisherService);
        withdrawalService.setWithdrawalRepository(withdrawalRepository);
    }

    @Test
    void getWithdrawalByInvestorId() {
        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> expectedWithdrawals = new ArrayList<>();
        expectedWithdrawals.add(withdrawal);
        when(withdrawalRepository.findWithdrawalsByInvestor_Id(1L)).thenReturn(expectedWithdrawals);

        List<Withdrawal> result = withdrawalService.getWithdrawalByInvestorId(1L);

        assertSame(expectedWithdrawals, result);
    }

    @Test
    void getWithdrawalByInvestorIdAndStatus() {
        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);

        when(withdrawalRepository.findWithdrawalsByInvestor_IdAndStatus(1L,WithdrawalStatus.STARTED)).thenReturn(withdrawals);
        List<Withdrawal> withdrawalList =  withdrawalService.getWithdrawalByInvestorIdAndStatus(1L,WithdrawalStatus.STARTED);
        assertNotNull(withdrawalList);
        assertEquals(2.00, withdrawalList.get(0).getWithdrawalAmount());
        assertEquals(withdrawalList.get(0).getStatus(),WithdrawalStatus.STARTED);
    }

    @Test
    void requestWithdrawal() throws WithdrawalLimitExceededException, MinimumWithdrawalAmountException, ProductNotFoundException, MinimumRequiredAgeException {
        Withdrawal withdrawal = createWithdrawal();
        withdrawal.setWithdrawalAmount(1.00);

        withdrawal.getInvestor().setDateOfBirth(Date.valueOf(LocalDate.of(1950, 1, 1)));
        when(withdrawalRepository.save(any(Withdrawal.class))).thenReturn(new Withdrawal());
        withdrawalService.requestWithdrawal(withdrawal.getInvestor(), ProductName.RETIREMENT, 1.00);
        verify(publisherService, times(1)).publishEvent(any(Withdrawal.class));
    }

    @Test
    void getWithdrawalByStatus() {
        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);

        when(withdrawalRepository.findWithdrawalsByStatus(WithdrawalStatus.STARTED)).thenReturn(withdrawals);
        List<Withdrawal> withdrawalList =  withdrawalService.getWithdrawalByStatus(WithdrawalStatus.STARTED);
        assertNotNull(withdrawalList);
        assertEquals(2.00, withdrawalList.get(0).getWithdrawalAmount());
        assertEquals(withdrawalList.get(0).getStatus(),WithdrawalStatus.STARTED);
    }

    @Test
    void updateWithdrawal() {

    }

    @Test
    void isWithdrawalAmountValidAndWithdrawalLimitExceededException() {

        Withdrawal withdrawal= createWithdrawal();
        withdrawal.getInvestor().setDateOfBirth(Date.valueOf(LocalDate.of(1950, 1, 1)));
        withdrawal.setWithdrawalAmount(2000.00);
        try
        {
            withdrawalService.requestWithdrawal(withdrawal.getInvestor(),withdrawal.getProduct().getName(),withdrawal.getWithdrawalAmount());
            fail("Expected SomeException, but no exception was thrown.");
        }
         catch (WithdrawalLimitExceededException | MinimumWithdrawalAmountException | MinimumRequiredAgeException |
                ProductNotFoundException e) {

            assertEquals(e.getMessage(),"The withdrawal is more than the product balance");
        }
    }


    @Test
    void createWithdrawalAndMinimumWithdrawalAmountException() {

        Withdrawal withdrawal= createWithdrawal();
        withdrawal.getInvestor().setDateOfBirth(Date.valueOf(LocalDate.of(1950, 1, 1)));
        withdrawal.setWithdrawalAmount(0.00);
        try
        {
            withdrawalService.requestWithdrawal(withdrawal.getInvestor(),withdrawal.getProduct().getName(),withdrawal.getWithdrawalAmount());
            fail("Expected SomeException, but no exception was thrown.");
        }
        catch (WithdrawalLimitExceededException | MinimumWithdrawalAmountException | MinimumRequiredAgeException |
               ProductNotFoundException e) {

            assertEquals(e.getMessage(),"Withdrawal amount should be more than 0.00");
        }
    }

    @Test
    void createWithdrawalAndMinimumRequiredAgeException() {

        Withdrawal withdrawal= createWithdrawal();
        withdrawal.setWithdrawalAmount(1.00);
        try
        {
            withdrawalService.requestWithdrawal(withdrawal.getInvestor(),withdrawal.getProduct().getName(),withdrawal.getWithdrawalAmount());
            fail("Expected SomeException, but no exception was thrown.");
        }
        catch (WithdrawalLimitExceededException | MinimumWithdrawalAmountException | MinimumRequiredAgeException |
               ProductNotFoundException e) {

            assertEquals(e.getMessage(),"Minimum age to withdraw from  retirement products is 65");
        }
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

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setWithdrawalAmount(2.00);
        withdrawal.setProduct(product);
        withdrawal.setStatus(WithdrawalStatus.STARTED);
        withdrawal.setInvestor(investor);

        return  withdrawal;
    }
}