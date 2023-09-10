package com.momentum.assesment.repository;

import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    List<Withdrawal> findWithdrawalsByInvestor_Id(long id);

    List<Withdrawal> findWithdrawalsByInvestor_IdAndStatus(long id, WithdrawalStatus status);

    List<Withdrawal> findWithdrawalsByStatus(WithdrawalStatus status);
}
