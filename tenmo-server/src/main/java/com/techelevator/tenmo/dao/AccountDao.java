package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    double getAccountBalanceByUserId(int userId);
    void moneyAddedToAccount(int accountIdTo, double amountToReceive);

    void moneyLeavesAccount(int accountIdFrom, double amountToSpend);

    void transferMoneyBetweenAccounts(int accountFrom, int accountTo, double amount);

    Account getAccountByUserId (int userId);
    Account getAccountByUserName (String userName);
    Account getAccountById (int accountId);
}
