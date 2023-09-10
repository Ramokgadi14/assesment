package com.momentum.assesment.repository;

import com.momentum.assesment.entities.Investor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InvestorRepositoryTest {

    @Mock
    private InvestorRepository investorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findById() {

        List<Investor> investors = new ArrayList<>();
        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");

        investors.add(investor);
        when(investorRepository.findById(1L)).thenReturn(investors);

        List<Investor> investorList = investorRepository.findById(1L);

        assertNotNull(investorList);
        assertEquals(1,investorList.get(0).getId());

    }

    @Test
    void findByEmail() {

        Investor investor = new Investor();
        investor.setId(1L);
        investor.setName("John");
        investor.setEmail("Joe@gmail.com");

        when(investorRepository.findByEmail("Joe@gmail.com")).thenReturn(investor);

        Investor investor1 = investorRepository.findByEmail("Joe@gmail.com");

        assertNotNull(investor1);
        assertEquals("Joe@gmail.com", investor1.getEmail());
    }
}