package com.momentum.assesment.repository;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Product;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WithdrawalRepositoryTest {

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findWithdrawalsByInvestor_Id() {
        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);

        when(withdrawalRepository.findWithdrawalsByInvestor_Id(1L)).thenReturn(withdrawals);
        List<Withdrawal> withdrawalList =  withdrawalRepository.findWithdrawalsByInvestor_Id(1L);
        assertNotNull(withdrawalList);
        assertEquals(2.00, withdrawalList.get(0).getWithdrawalAmount());
    }

    @Test
    void findWithdrawalsByInvestor_IdAndStatus() {

        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);

        when(withdrawalRepository.findWithdrawalsByInvestor_IdAndStatus(1L,WithdrawalStatus.STARTED)).thenReturn(withdrawals);
        List<Withdrawal> withdrawalList =  withdrawalRepository.findWithdrawalsByInvestor_IdAndStatus(1L,WithdrawalStatus.STARTED);
        assertNotNull(withdrawalList);
        assertEquals(2.00, withdrawalList.get(0).getWithdrawalAmount());
        assertEquals(withdrawalList.get(0).getStatus(),WithdrawalStatus.STARTED);
    }

    @Test
    void findWithdrawalsByStatus() {

        Withdrawal withdrawal = createWithdrawal();
        List<Withdrawal> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal);

        when(withdrawalRepository.findWithdrawalsByStatus(WithdrawalStatus.STARTED)).thenReturn(withdrawals);
        List<Withdrawal> withdrawalList =  withdrawalRepository.findWithdrawalsByStatus(WithdrawalStatus.STARTED);
        assertNotNull(withdrawalList);
        assertEquals(2.00, withdrawalList.get(0).getWithdrawalAmount());
        assertEquals(withdrawalList.get(0).getStatus(),WithdrawalStatus.STARTED);
    }

    private Withdrawal createWithdrawal()
    {
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");

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

        return  withdrawal;
    }
}