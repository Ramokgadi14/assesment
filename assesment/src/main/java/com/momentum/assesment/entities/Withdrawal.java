package com.momentum.assesment.entities;

import com.momentum.assesment.entities.enums.WithdrawalStatus;
import jakarta.persistence.*;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 1, message = "minimum amount to withdraw is 1.00")
    private double withdrawalAmount;

    private LocalDateTime withdrawalDateTime;
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;
    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investor;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public LocalDateTime getWithdrawalDateTime() {
        return withdrawalDateTime;
    }

    public void setWithdrawalDateTime(LocalDateTime withdrawalDateTime) {
        this.withdrawalDateTime = withdrawalDateTime;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
