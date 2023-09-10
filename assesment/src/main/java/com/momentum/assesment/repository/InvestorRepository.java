package com.momentum.assesment.repository;

import com.momentum.assesment.entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {

    List<Investor> findById(long id);

    Investor findByEmail(String email);
}
