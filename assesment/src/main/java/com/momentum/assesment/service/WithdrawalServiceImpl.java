package com.momentum.assesment.service;

import com.momentum.assesment.entities.Investor;
import com.momentum.assesment.entities.Product;
import com.momentum.assesment.entities.Withdrawal;
import com.momentum.assesment.entities.enums.ProductName;
import com.momentum.assesment.entities.enums.WithdrawalStatus;
import com.momentum.assesment.exception.MinimumRequiredAgeException;
import com.momentum.assesment.exception.MinimumWithdrawalAmountException;
import com.momentum.assesment.exception.ProductNotFoundException;
import com.momentum.assesment.exception.WithdrawalLimitExceededException;
import com.momentum.assesment.repository.WithdrawalRepository;
import com.momentum.assesment.service.event.EventPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {


    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private EventPublisherService publisherService;

    @Override
    public List<Withdrawal> getWithdrawalByInvestorId(long id) {
        return withdrawalRepository.findWithdrawalsByInvestor_Id(id);
    }

    @Override
    public List<Withdrawal> getWithdrawalByInvestorIdAndStatus(long id, WithdrawalStatus status) {
        return withdrawalRepository.findWithdrawalsByInvestor_IdAndStatus(id, status);
    }


    @Override
    public void requestWithdrawal(Investor investor, ProductName productName, double withdrawalAmount) throws MinimumWithdrawalAmountException, MinimumRequiredAgeException, WithdrawalLimitExceededException, ProductNotFoundException {

        // Throw error if amount 0, and the no need other checks
        if (withdrawalAmount == 0.00) {
            throw new MinimumWithdrawalAmountException("Withdrawal amount should be more than 0.00");
        }
        Product product = getProductByName(investor.getProducts(), productName);

        if (product.getName().equals(ProductName.RETIREMENT)) {
            if (!isAgeEligibleForWithForWithdrawal(investor.getDateOfBirth())) {
                throw new MinimumRequiredAgeException("Minimum age to withdraw from  retirement products is 65");
            }

            if (isWithdrawalAmountValid(product.getBalance(), withdrawalAmount)) {
                throw new WithdrawalLimitExceededException("The withdrawal is more than the product balance");
            }

            if (isWithdrawalAmountPercentageValid(product.getBalance(), withdrawalAmount)) {
                throw new WithdrawalLimitExceededException("Withdrawal are limited up to 90% of the product balance,Maximum amount you can withdraw is " + String.format("%.2f", (0.9 * product.getBalance())));
            }


            Withdrawal withdrawal = createWithdrawalObject(investor, product, withdrawalAmount);
            publisherService.publishEvent(withdrawal);
        } else {
            // this is for savings withdrawal and any other product

            //check if the withdrawal doesn't exceed the product balance
            if (isWithdrawalAmountValid(product.getBalance(), withdrawalAmount)) {
                throw new WithdrawalLimitExceededException("The withdrawal is more than the product balance");
            }

            //checking if the withdrawal amount is not more 90%
            if (isWithdrawalAmountPercentageValid(product.getBalance(), withdrawalAmount)) {
                throw new WithdrawalLimitExceededException("Withdrawal are limited up to 90% of the product balance,Maximum amount you can withdraw is " + String.format("%.2f", (0.9 * product.getBalance())));
            }


            Withdrawal withdrawal = createWithdrawalObject(investor, product, withdrawalAmount);

            //trigger external event, to through all the steps and store the audit trail
            publisherService.publishEvent(withdrawal);
        }
    }

    @Override
    public List<Withdrawal> getWithdrawalByStatus(WithdrawalStatus status) {
        return withdrawalRepository.findWithdrawalsByStatus(status);
    }

    @Override
    public Withdrawal updateWithdrawal(Withdrawal withdrawal) {
        return withdrawalRepository.save(withdrawal);
    }

    //create Withdrawal object, save to DB
    public Withdrawal createWithdrawalObject(Investor investor, Product product, double withdrawalAmount) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setInvestor(investor);
        withdrawal.setWithdrawalDateTime(LocalDateTime.now());
        withdrawal.setProduct(product);
        withdrawal.setWithdrawalAmount(withdrawalAmount);
        withdrawal.setStatus(WithdrawalStatus.STARTED);
        return withdrawalRepository.save(withdrawal);
    }


    //checking the required age for Retirement product withdrawal
    private static boolean isAgeEligibleForWithForWithdrawal(Date dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dateOfBirth.toLocalDate(), currentDate).getYears();
        return age > 65;
    }

    public boolean isWithdrawalAmountValid(double ProductBalance, double withdrawalAmount) {
        return withdrawalAmount > ProductBalance;
    }

    public boolean isWithdrawalAmountPercentageValid(double ProductBalance, double withdrawalAmount) {
        double maxWithdrawal = 0.9 * ProductBalance;
        System.out.println("ProductBalance = " + ProductBalance + ", withdrawalAmount = " + withdrawalAmount);
        System.out.println("MAX withdrawal " + maxWithdrawal);
        return withdrawalAmount > maxWithdrawal;
    }

    private Product getProductByName(List<Product> productList, ProductName productName) throws ProductNotFoundException {
        Product product = null;

        for (Product product1 : productList) {
            if (product1.getName().equals(productName)) {
                product = product1;
                break;
            }
        }

        if (product == null) {
            throw new ProductNotFoundException("Product with name " + productName.name() + " not found");
        } else {
            return product;
        }
    }

    public void setWithdrawalRepository(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    public void setPublisherService(EventPublisherService publisherService) {
        this.publisherService = publisherService;
    }
}
