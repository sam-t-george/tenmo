package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public TenmoController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path="/account/balance", method= RequestMethod.GET)
    public BigDecimal getAccountBalanceByUserId(Principal principal) {
        User currentUser = userDao.getUserByUsername(principal.getName());
        BigDecimal balance = accountDao.getAccountByUserId(currentUser.getId()).getBalance();
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
    public Transfer sendBucks (Principal principal, @RequestBody Transfer transfer) {
            User userFrom = userDao.getUserByUsername(principal.getName());
            User userTo = userDao.getUserById(transfer.getUserIdTo());
            transfer = transferDao.createTransfer(transfer);
            accountDao.transferMoneyBetweenAccounts(userFrom.getId(), userTo.getId(), transfer.getAmount());
            return transfer;
    }

    @RequestMapping(path="/my_transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountFrom(Principal principal) {
        User currentUser = userDao.getUserByUsername(principal.getName());
        return transferDao.getTransfersByAccountFrom(currentUser.getId());
    }

    @RequestMapping(path = "/transfer_details/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfer;
        }
    }


    @RequestMapping(path="/user/{user_id}", method= RequestMethod.GET)
    public User getUserById (@PathVariable int user_id) {
        return userDao.getUserById(user_id);
    }

}