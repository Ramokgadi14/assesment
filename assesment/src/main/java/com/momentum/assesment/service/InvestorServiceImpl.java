package com.momentum.assesment.service;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.exception.InvestorNotFoundException;
import com.momentum.assesment.repository.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestorServiceImpl implements InvestorService {

    @Autowired
    private InvestorRepository investorRepository;

    @Override
    public List<Investor> getInvestorById(long id) {
        return investorRepository.findById(id);
    }

    @Override
    public Investor getInvestorByEmail(String email) throws InvestorNotFoundException {
        Investor investor = investorRepository.findByEmail(email);
        if (investor == null) {
            throw new InvestorNotFoundException("Investor with email: " + email + "  not found");
        } else {
            return investor;
        }
    }

    public void setInvestorRepository(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }
}
