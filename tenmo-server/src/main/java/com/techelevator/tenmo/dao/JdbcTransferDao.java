package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    public JdbcTransferDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void transferMoneyBetweenAccounts(int userIdFrom, int userIdTo, double amount) {
        accountDao.moneyLeavesAccount(amount, userIdFrom);
        accountDao.moneyAddedToAccount(amount, userIdTo);
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer newTransfer = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, int.class,
                    newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(), newTransfer.getUserIdFrom(),
                    newTransfer.getUserIdTo(), newTransfer.getAmount());
            newTransfer = getTransferById(newTransferId);
            return newTransfer;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    public Transfer mapRowToTransfer (SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setUserIdFrom(rowSet.getInt("account_from"));
        transfer.setUserIdTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getDouble("amount"));
        return transfer;
    }

}
