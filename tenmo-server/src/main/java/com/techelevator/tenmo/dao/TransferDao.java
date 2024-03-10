package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
//    void transferMoneyBetweenAccounts(int userIdFrom, int userIdTo, double amount);
    Transfer getTransferById(int transferId);
    Transfer createTransfer(Transfer transfer);
}
