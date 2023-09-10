package com.momentum.assesment.service;


import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import com.momentum.assesment.exception.MinimumRequiredAgeException;
import com.momentum.assesment.exception.MinimumWithdrawalAmountException;
import com.momentum.assesment.exception.ProductNotFoundException;
import com.momentum.assesment.exception.WithdrawalLimitExceededException;

import java.util.List;

public interface WithdrawalService {

    List<Withdrawal> getWithdrawalByInvestorId(long id);

    List<Withdrawal> getWithdrawalByInvestorIdAndStatus(long id, WithdrawalStatus status);

    void requestWithdrawal(Investor investor, ProductName productName, double withdrawalAmount) throws MinimumWithdrawalAmountException,
            MinimumRequiredAgeException, WithdrawalLimitExceededException, ProductNotFoundException;

    List<Withdrawal> getWithdrawalByStatus(WithdrawalStatus status);

    Withdrawal updateWithdrawal(Withdrawal withdrawal);

}
