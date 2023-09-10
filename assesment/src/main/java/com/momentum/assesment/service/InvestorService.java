package com.momentum.assesment.service;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.exception.InvestorNotFoundException;

import java.util.List;

public interface InvestorService {

    List<Investor> getInvestorById(long id);

    Investor getInvestorByEmail(String email) throws InvestorNotFoundException;

}
