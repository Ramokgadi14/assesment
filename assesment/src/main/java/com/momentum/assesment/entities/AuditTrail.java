package com.momentum.assesment.entities;

import com.momentum.assesment.entities.enums.WithdrawalStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "audit_trail")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    private long investorId;
    private long withdrawalId;
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus withdrawalStatus;
    private double withdrawalAmount;
    private double productPreviousBalance;
    private double productNewBalance;
    private LocalDateTime date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(long investorId) {
        this.investorId = investorId;
    }

    public long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public double getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public double getProductPreviousBalance() {
        return productPreviousBalance;
    }

    public void setProductPreviousBalance(double productPreviousBalance) {
        this.productPreviousBalance = productPreviousBalance;
    }

    public double getProductNewBalance() {
        return productNewBalance;
    }

    public void setProductNewBalance(double productNewBalance) {
        this.productNewBalance = productNewBalance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
