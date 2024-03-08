package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;


public class Transfer {
    @JsonProperty("transfer_id")
    @NotNull
    private int transferId;

    @JsonProperty("transfer_type_id")
    @NotNull
    private int transferTypeId;

    @JsonProperty("transfer_status_id")
    private int transferStatusId;

    @JsonProperty("account_from")
    @NotNull
    private int accountFrom;

    @JsonProperty("account_to")
    @NotNull
    private int accountTo;

    @JsonProperty("amount")
    @NotNull
    private double amount;

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, double amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
