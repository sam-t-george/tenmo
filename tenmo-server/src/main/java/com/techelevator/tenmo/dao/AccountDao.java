package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    double getAccountBalanceByUserId(int userId);
}
