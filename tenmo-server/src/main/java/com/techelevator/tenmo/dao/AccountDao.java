package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    double getAccountBalanceByUserId(int userId);
    void moneyAddedToAccount(int accountIdTo, double amountToReceive);

    void moneyLeavesAccount(int accountIdFrom, double amountToSpend);

    Account getAccountByUserId (int userId);
}
