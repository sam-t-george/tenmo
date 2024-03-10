package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
//    double getAccountBalanceByUserId(int userId);
    void moneyAddedToAccount(BigDecimal amountToReceive, int userIdTo);

    void moneyLeavesAccount(BigDecimal amountToSpend, int userIdFrom);

    void transferMoneyBetweenAccounts(int userIdFrom, int userIdTo, BigDecimal amount);




    Account getAccountByUserId (int userId);
//    Account getAccountByUserName (String userName);
    Account getAccountById (int accountId);
}
