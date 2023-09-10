package com.momentum.assesment.service;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.exception.InvestorNotFoundException;
import com.momentum.assesment.repository.InvestorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InvestorServiceImplTest {

    @Mock
    private InvestorRepository investorRepository;

    private InvestorServiceImpl investorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        investorService = new InvestorServiceImpl();
        investorService.setInvestorRepository(investorRepository);

    }

    @Test
    void getInvestorById() {
        List<Investor> investors = new ArrayList<>();
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");
        investorRepository.saveAndFlush(investor);

        investors.add(investor);
        when(investorService.getInvestorById(1L)).thenReturn(investors);

        List<Investor> investorList = investorService.getInvestorById(1L);

        assertNotNull(investorList);
        assertEquals(1,investorList.get(0).getId());
    }

    @Test
    void getInvestorByEmail() throws InvestorNotFoundException {

        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");
        investorRepository.saveAndFlush(investor);

        when(investorRepository.findByEmail("Joe@gmail.com")).thenReturn(investor);

        Investor investor1 = investorService.getInvestorByEmail("Joe@gmail.com");

        assertNotNull(investor1);
        assertEquals("Joe@gmail.com", investor1.getEmail());
    }



    @Test
    void getInvestorByEmailNotFound() {

       try{
            investorService.getInvestorByEmail("Kpe@g.com");
            fail("Expected SomeException, but no exception was thrown.");
       }
       catch ( InvestorNotFoundException e)
       {
         assertEquals("Investor with email: Kpe@g.com  not found", e.getMessage());
       }
    }
}