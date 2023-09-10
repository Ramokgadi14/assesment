package com.momentum.assesment.service.event;

import com.momentum.assesment.entities.AuditTrail;
import com.momentum.assesment.entities.Product;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import com.momentum.assesment.repository.AuditTrailRepository;
import com.momentum.assesment.repository.ProductRepository;
import com.momentum.assesment.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class EventListenerService {

    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private AuditTrailRepository auditTrailRepository;
    @Autowired
    ProductRepository productRepository;

    @EventListener
    public void handleCustomEvent(CustomEvent event) {
        // Process the event and update the database
        processWithdrawal(event.getWithdrawal());
    }

    private void processWithdrawal(Withdrawal unprocessedWithdrawal) {
        // processing the withdrawal, to go through all the steps
        Withdrawal withdrawal = unprocessedWithdrawal;
        if (withdrawal != null) {
            System.out.println("Withdrawal Process Starting:");
            auditTrailRepository.save(createTrail(withdrawal));

            System.out.println("Withdrawal Moving to -->" + WithdrawalStatus.EXECUTING.name());

            withdrawal.setStatus(WithdrawalStatus.EXECUTING);
            withdrawal = withdrawalService.updateWithdrawal(withdrawal);

            Product product = withdrawal.getProduct();
            double balance = product.getBalance();
            double newBalance = product.getBalance() - withdrawal.getWithdrawalAmount();
            product.setBalance(newBalance);
            product = productRepository.save(product);
            //update Withdrawal obj with new product values
            withdrawal.setProduct(product);

            //update audit
            AuditTrail auditTrail = getAuditTrail(withdrawal, newBalance, balance);
            auditTrailRepository.save(auditTrail);
            withdrawalService.updateWithdrawal(withdrawal);

            System.out.println("Withdrawal Moving to -->" + WithdrawalStatus.DONE.name());

            withdrawal.setStatus(WithdrawalStatus.DONE);
            auditTrail = getAuditTrail(withdrawal, newBalance, balance);
            auditTrailRepository.save(auditTrail);
            withdrawalService.updateWithdrawal(withdrawal);

            System.out.println("Withdrawal Completed!!!");
        }
    }

    private AuditTrail getAuditTrail(Withdrawal withdrawal, double newBalance, double balance) {
        AuditTrail auditTrail = createTrail(withdrawal);
        auditTrail.setProductNewBalance(newBalance);
        auditTrail.setProductPreviousBalance(balance);
        return auditTrail;
    }

    private AuditTrail createTrail(Withdrawal withdrawal) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setInvestorId(withdrawal.getInvestor().getId());
        auditTrail.setProductId(withdrawal.getProduct().getId());
        auditTrail.setWithdrawalAmount(withdrawal.getWithdrawalAmount());

        auditTrail.setProductNewBalance(withdrawal.getProduct().getBalance());
        auditTrail.setProductPreviousBalance(withdrawal.getProduct().getBalance());
        auditTrail.setWithdrawalStatus(withdrawal.getStatus());
        auditTrail.setDate(LocalDateTime.now());
        return auditTrail;
    }



}
