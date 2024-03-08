package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;


    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public double getAccountBalanceByUserId(int userId) {
        double balance = 0;
        String sql = "SELECT balance " +
                "FROM account WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            balance = rowSet.getDouble("balance");
        }
        return balance;
    }

    @Override
    public void moneyLeavesAccount(int accountIdFrom, double amountToSpend) {
        String sql = "UPDATE account SET balance = (balance - ?) WHERE account_id = ?;";
        jdbcTemplate.update(sql, amountToSpend, accountIdFrom);
    }
    @Override
    public void moneyAddedToAccount(int accountIdTo, double amountToReceive) {
        String sql = "UPDATE account SET balance = (balance + ?) WHERE account_id = ?;";
        jdbcTemplate.update(sql, accountIdTo, amountToReceive);
    }

    @Override
    public void transferMoneyBetweenAccounts(int accountFrom, int accountTo, double amount) {
        moneyLeavesAccount(accountFrom, amount);
        moneyAddedToAccount(accountTo, amount);
    }

    @Override
    public Account getAccountByUserId (int userId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    @Override
    public Account getAccountByUserName (String userName) {
        Account account = null;
        String sql = "SELECT a.account_id, a.user_id, a.balance " +
                "FROM account a " +
                "JOIN tenmo_user ON a.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userName);
        while (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }
    @Override
    public Account getAccountById (int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        while (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setBalance(rowSet.getDouble("balance"));
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        return account;
    }

}
