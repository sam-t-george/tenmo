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
    public JdbcTransferDao (JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

//    @Override
//    public void transferMoneyBetweenAccounts(int userIdFrom, int userIdTo, double amount) {
//        accountDao.moneyLeavesAccount(amount, userIdFrom);
//        accountDao.moneyAddedToAccount(amount, userIdTo);
//    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer newTransfer = null;
        String sql =
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (" +
                    "?, " +
                    "?, " +
                    "(SELECT account_id FROM account WHERE user_id = ?), " +
                    "(SELECT account_id FROM account WHERE user_id = ?), " +
                    "?" +
                ") " +
                "RETURNING transfer_id;";
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, int.class,
                    2, 2, transfer.getUserIdFrom(),
                    transfer.getUserIdTo(), transfer.getAmount());
            newTransfer = getTransferById(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newTransfer;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql =
                "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if (rowSet.next()) {
                transfer = mapRowToTransfer(rowSet);
            }
            return transfer;
    }

    @Override
    public List<Transfer> getTransfersByAccountFrom(int currentUserId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =
                "SELECT " +
                    "t.transfer_id, " +
                    "t.transfer_type_id, " +
                    "t.transfer_status_id, " +
                    "t.account_from, " +
                    "t.account_to, " +
                    "t.amount " +
                "FROM transfer t " +
                "JOIN account a ON a.account_id = t.account_from " +
                "WHERE a.user_id = ? " +
                "ORDER BY t.transfer_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUserId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    public Transfer mapRowToTransfer (SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setUserIdFrom(
                accountDao.getAccountById(rowSet.getInt("account_from")).getUserId()
        );
        transfer.setUserIdTo(
                accountDao.getAccountById(rowSet.getInt("account_to")).getUserId()
        );
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

}
