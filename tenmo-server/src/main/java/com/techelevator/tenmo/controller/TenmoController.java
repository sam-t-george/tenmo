package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.Transfer;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    private AccountDao accountDao;
    private UserDao userDao;

    public TenmoController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path="/account/balance", method= RequestMethod.GET)
    public double getAccountBalanceByUserId(Principal principal) {
        User currentUser = userDao.getUserByUsername(principal.getName());
        double balance = accountDao.getAccountBalanceByUserId(currentUser.getId());
        return balance;
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> getAllUsersExceptMyself(Principal principal) {
        List<User> allUsers = userDao.getUsers();
        List<User> allUsersExceptMyself = new ArrayList<>();
        User currentUser = userDao.getUserByUsername(principal.getName());
        for (User user : allUsers) {
            if ( !(user.equals(currentUser)) ) {
                allUsersExceptMyself.add(user);
            }
        }
        return allUsersExceptMyself;
    }

    @RequestMapping(path="/send_bucks", method = RequestMethod.POST)
    public void sendBucks (Principal principal, @RequestBody Transfer transfer) {
        Account senderAccount = accountDao.getAccountByUserName(principal.getName());
        Account receiverAccount = accountDao.getAccountById(transfer.getAccountTo());
        accountDao.transferMoneyBetweenAccounts(senderAccount.getAccountId(),
                receiverAccount.getAccountId(), transfer.getAmount());
    }

//    @RequestMapping(path="/accounts/my_account")
//    public Account getAccountByUserId (Principal principal) {
//        User currentUser = userDao.getUserByUsername(principal.getName());
//        //TODO ADD TO TRANSFER TABLE
//        return accountDao.getAccountByUserId(currentUser.getId());
//    }

}