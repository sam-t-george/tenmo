package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {
//    double getAccountBalanceByUserId(int userId);
    void moneyAddedToAccount(double amountToReceive, int userIdTo);

    void moneyLeavesAccount(double amountToSpend, int userIdFrom);

    void transferMoneyBetweenAccounts(int userIdFrom, int userIdTo, double amount);




    Account getAccountByUserId (int userId);
//    Account getAccountByUserName (String userName);
//    Account getAccountById (int accountId);
}
